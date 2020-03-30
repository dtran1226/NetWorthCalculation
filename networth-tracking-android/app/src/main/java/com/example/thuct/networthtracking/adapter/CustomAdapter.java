package com.example.thuct.networthtracking.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuct.networthtracking.R;
import com.example.thuct.networthtracking.activity.NetWorthTrackingActivity;
import com.example.thuct.networthtracking.model.Asset;
import com.example.thuct.networthtracking.model.CalculationRequest;
import com.example.thuct.networthtracking.model.CalculationResponse;
import com.example.thuct.networthtracking.model.Liability;
import com.example.thuct.networthtracking.model.MoneyRecord;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    // List of money records (assets or liabilities) for current ListView
    List<MoneyRecord> moneyRecords;
    // Full list of all money records to request to server
    public static List<MoneyRecord> fullMoneyRecords;
    NetWorthTrackingActivity activity;
    DecimalFormat df;
    final String NETWORTH_CALCULATION_API_URL_EMULATOR = "http://10.0.2.2:8080/calculate";
    final String NETWORTH_CALCULATION_API_URL_DEVICE = "http://127.0.0.1:8080/calculate";
    final double MAX_MONEY_AMOUNT = 9999999.99;
    final String DEFAULT_SERVER_EXCEPTION = "Failed to connect to server!";

    public CustomAdapter(Context context, List<MoneyRecord> moneyRecords, List<MoneyRecord> fullMoneyRecords, NetWorthTrackingActivity activity) {
        this.context = context;
        this.moneyRecords = moneyRecords;
        this.fullMoneyRecords = fullMoneyRecords;
        this.activity = activity;
        // Decimal money format
        df = new DecimalFormat("#,###.##");
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public Object getItem(int i) {
        return moneyRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return moneyRecords.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.money_record, null);
        final TextView tvMoneyRecord = view.findViewById(R.id.tv_money_record);
        final EditText edtMoneyRecord = view.findViewById(R.id.edt_money_record);
        // Set text for money records
        final MoneyRecord moneyRecord = moneyRecords.get(i);
        tvMoneyRecord.setText(moneyRecord.getName());
        edtMoneyRecord.setText(String.valueOf(moneyRecords.get(i).getAmount()));
        // Add textChangedListener to handle server request when account line amount is edited
        edtMoneyRecord.addTextChangedListener(new NumberTextWatcher(edtMoneyRecord, moneyRecord));
        return view;
    }

    /*
    Self-defined TextWatcher to handle text changes on EditText
     */
    public class NumberTextWatcher implements TextWatcher {
        EditText editText;
        MoneyRecord moneyRecord;
        Timer timer = new Timer();
        boolean firstEditFlag = true;
        // Delay time to handle after text changed
        final long delay = 500; // in ms

        public NumberTextWatcher(EditText editText, MoneyRecord moneyRecord) {
            this.editText = editText;
            this.moneyRecord = moneyRecord;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (timer != null)
                timer.cancel();
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            editText.removeTextChangedListener(this);
            try {
                // Set Edit Text and keep track of money amount to user later inside in classes
                final double editedVal = setEditText(firstEditFlag, editText, editable);
                firstEditFlag = false;
                // Handle server request when account line amount is edited
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        List<Double> assets = new ArrayList<>();
                        List<Double> liabilities = new ArrayList<>();
                        for (int j = 0; j < fullMoneyRecords.size(); j++) {
                            // Update newly edited money record to full money records
                            if (fullMoneyRecords.get(j).getName().equals(moneyRecord.getName())) {
                                moneyRecord.setAmount(editedVal);
                                fullMoneyRecords.set(j, moneyRecord);
                            }
                            // Divide full money records to suitable assets and liabilities list (amount only)
                            // as required parameters to request to server
                            if (fullMoneyRecords.get(j) instanceof Asset) {
                                assets.add(fullMoneyRecords.get(j).getAmount());
                            } else if (fullMoneyRecords.get(j) instanceof Liability) {
                                liabilities.add(fullMoneyRecords.get(j).getAmount());
                            }
                        }
                        // Call getCalculationResponse API on server
                        CalculationRequest calculationRequest = new CalculationRequest(assets, liabilities);
                        new GetNetWorthTask().execute(calculationRequest);
                    }
                }, delay);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            editText.addTextChangedListener(this);
        }
    }

    class GetNetWorthTask extends AsyncTask<CalculationRequest, Void, CalculationResponse> {

        @Override
        protected CalculationResponse doInBackground(CalculationRequest... calculationRequests) {
            final CalculationRequest calculationRequest = calculationRequests[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                // Set up RestTemplate to call getCalculationResponse API
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<CalculationRequest> requestEntity = new HttpEntity<>(calculationRequest, requestHeaders);
                ResponseEntity<CalculationResponse> responseEntity = restTemplate.exchange(NETWORTH_CALCULATION_API_URL_EMULATOR, HttpMethod.POST, requestEntity,
                        CalculationResponse.class);
                return responseEntity.getBody();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(CalculationResponse calculationResponse) {
            if (activity != null) {
                // Update text views of total assets, total liabilities and net worth
                if (calculationResponse != null) {
                    activity.updateTextViews(calculationResponse.getTotalAssets(), calculationResponse.getTotalLiabilities(), calculationResponse.getNetWorth());
                } else {
                    Toast.makeText(context, DEFAULT_SERVER_EXCEPTION, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
    Get valid money amount (return maximum amount if input amount is too big)
     */
    private double getValidMoneyAmount(double amount) {
        if (amount > MAX_MONEY_AMOUNT) {
            amount = MAX_MONEY_AMOUNT;
        }
        return amount;
    }

    /*
    Set Edit Text with valid decimal money format
     */
    private double setEditText(Boolean firstEdit, EditText editText, Editable editable) {
        String originalString = editable.toString();
        double moneyAmount;
        // Set default decimal money value if edit text is empty
        if (originalString.isEmpty()) {
            originalString = "0.0";
        }
        // Remove all ',' from edit text to parse to double
        if (originalString.contains(",")) {
            originalString = originalString.replaceAll(",", "");
        }
        moneyAmount = Double.parseDouble(originalString);
        // If it's the first time user edits
        if (firstEdit) {
            // User adds first number of integer part (money amount >= 10), remove default '0' before dot
            if (moneyAmount >= 10) {
                moneyAmount /= 10;
            }
        }
        // Get valid money amount
        moneyAmount = getValidMoneyAmount(moneyAmount);
        // Format to money format
        String formattedString = df.format(moneyAmount);
        // // Set default decimal money value with prefix '.0'
        if (!formattedString.contains(".")) {
            formattedString += ".0";
        }
        // Setting text after format to EditText
        editText.setText(formattedString);
        // Set cursor position (before dot)
        String strFromDotToEnd = formattedString.substring(formattedString.indexOf("."), formattedString.length());
        editText.setSelection(formattedString.length() - strFromDotToEnd.length());
        return moneyAmount;
    }
}
