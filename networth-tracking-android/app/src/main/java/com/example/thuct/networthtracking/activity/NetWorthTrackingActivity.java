package com.example.thuct.networthtracking.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuct.networthtracking.R;
import com.example.thuct.networthtracking.adapter.CustomAdapter;
import com.example.thuct.networthtracking.model.Asset;
import com.example.thuct.networthtracking.model.Liability;
import com.example.thuct.networthtracking.model.MoneyRecord;
import com.example.thuct.networthtracking.model.User;
import com.example.thuct.networthtracking.utility.Utility;

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

public class NetWorthTrackingActivity extends AppCompatActivity {
    TextView tvGreeting;
    CustomAdapter customAdapter;
    ListView lvCashInvestAssets;
    ListView lvLongTermAssets;
    ListView lvOtherAssets;
    ListView lvShortTermLiabilities;
    ListView lvLongTermDebts;
    TextView tvNetWorth;
    TextView tvTotalAssets;
    TextView tvTotalLiabilities;
    Button btnSave;
    List<MoneyRecord> sampleMoneyRecords;
    DecimalFormat df;
    String userName;
    long id = 0;
    final String SHARED_PREFERENCE_NAME = "MyPref";
    final String SHARED_PREFERENCE_USER_NAME = "userName";
    final String GREETING_HELLO = "Hello ";
    final String GREETING_EXCLAMATION = "!";
    final String CHEQUING = "Chequing";
    final String SAVINGS_FOR_TAXS = "Savings for Taxes";
    final String RAINY_DAY_FUND = "Rainy Day Fund";
    final String SAVINGS_FOR_FUN = "Savings for Fun";
    final String SAVINGS_FOR_TRAVEL = "Savings for Travel";
    final String SAVINGS_FOR_PERSONAL_DEVELOPMENT = "Savings for Personal Development";
    final String INVESTMENT_1 = "Investment 1";
    final String INVESTMENT_2 = "Investment 2";
    final String INVESTMENT_3 = "Investment 3";
    final String INVESTMENT_4 = "Investment 4";
    final String INVESTMENT_5 = "Investment 5";
    final String PRIMARY_HOME = "Primary Home";
    final String SECOND_HOME = "Second Home";
    final String CREDIT_CARD_1 = "Credit Card 1";
    final String CREDIT_CARD_2 = "Credit Card 2";
    final String OTHERS = "Others";
    final String MORtGAGE_1 = "Mortgage 1";
    final String MORtGAGE_2 = "Mortgage 2";
    final String LINE_OF_CREDIT = "Line of Credit";
    final String INVESTMENT_LOAN = "Investment Loan";
    final String STUDENT_LOAN = "Student Loan";
    final String CAR_LOAN = "Car Loan";
    final double DEFAULT_AMOUNT = 0;
    final String USER_API_URL_EMULATOR = "http://10.0.2.2:8080/user";
    final String USER_API_URL_DEVICE = "http://127.0.0.1:8080/user";
    final String DEFAULT_SERVER_EXCEPTION = "Failed to connect to server!";
    final String SAVE_DATA_SUCCESSFULLY = "Data is saved successfully";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_tracking);
        // Set default format text for money amount
        df = new DecimalFormat("#,###.##");
        // Set greeting text including user's name for header
        tvGreeting = findViewById(R.id.tv_greeting);
        userName = Utility.getStringFromSharePref(getApplicationContext(), SHARED_PREFERENCE_NAME, SHARED_PREFERENCE_USER_NAME);
        tvGreeting.setText(String.format("%s%s%s", GREETING_HELLO, userName, GREETING_EXCLAMATION));
        // Prepare money records for List views
        prepareDefaultMoneyRecords();
        // Fill data to assets and liabilities list views
        List<MoneyRecord> cashInvestAssets = new ArrayList<>();
        List<MoneyRecord> longTermAssets = new ArrayList<>();
        List<MoneyRecord> otherAssets = new ArrayList<>();
        List<MoneyRecord> shortTermLiabilities = new ArrayList<>();
        List<MoneyRecord> longTermDebts = new ArrayList<>();
        // Divide assets and liabilities to sub-category lists
        for (MoneyRecord moneyRecord : sampleMoneyRecords) {
            if (moneyRecord instanceof Asset) {
                Asset asset = (Asset) moneyRecord;
                switch (asset.getType()) {
                    case CASH:
                        cashInvestAssets.add(asset);
                        break;
                    case INVESTMENT:
                        cashInvestAssets.add(asset);
                        break;
                    case LONG_TERM_ASSET:
                        longTermAssets.add(asset);
                        break;
                    case OTHER:
                        otherAssets.add(asset);
                        break;
                    default:
                        break;
                }
            } else if (moneyRecord instanceof Liability) {
                Liability liability = (Liability) moneyRecord;
                switch (liability.getType()) {
                    case SHORT_TERM_LIABILITY:
                        shortTermLiabilities.add(liability);
                        break;
                    case LONG_TERM_DEBT:
                        longTermDebts.add(liability);
                        break;
                    default:
                        break;
                }
            }
        }
        // Fill cash and investment assets list
        lvCashInvestAssets = findViewById(R.id.lv_cash_investment);
        customAdapter = new CustomAdapter(getApplicationContext(), cashInvestAssets, sampleMoneyRecords, NetWorthTrackingActivity.this);
        lvCashInvestAssets.setAdapter(customAdapter);
        // Fill long term assets list
        lvLongTermAssets = findViewById(R.id.lv_long_term_assets);
        customAdapter = new CustomAdapter(getApplicationContext(), longTermAssets, sampleMoneyRecords, NetWorthTrackingActivity.this);
        lvLongTermAssets.setAdapter(customAdapter);
        // Fill other assets list
        lvOtherAssets = findViewById(R.id.lv_other);
        customAdapter = new CustomAdapter(getApplicationContext(), otherAssets, sampleMoneyRecords, NetWorthTrackingActivity.this);
        lvOtherAssets.setAdapter(customAdapter);
        // Fill short term liabilities list
        lvShortTermLiabilities = findViewById(R.id.lv_short_term_liabilities);
        customAdapter = new CustomAdapter(getApplicationContext(), shortTermLiabilities, sampleMoneyRecords, NetWorthTrackingActivity.this);
        lvShortTermLiabilities.setAdapter(customAdapter);
        // Fill long term debts list
        lvLongTermDebts = findViewById(R.id.lv_long_term_debts);
        customAdapter = new CustomAdapter(getApplicationContext(), longTermDebts, sampleMoneyRecords, NetWorthTrackingActivity.this);
        lvLongTermDebts.setAdapter(customAdapter);
        // Set default total assets
        tvTotalAssets = findViewById(R.id.tv_total_assets);
        tvTotalAssets.setText(String.valueOf(DEFAULT_AMOUNT));
        // Set default total liabilities
        tvTotalLiabilities = findViewById(R.id.tv_total_liabilities);
        tvTotalLiabilities.setText(String.valueOf(DEFAULT_AMOUNT));
        // Set default net worth
        tvNetWorth = findViewById(R.id.tv_net_worth);
        tvNetWorth.setText(String.valueOf(DEFAULT_AMOUNT));
        // Justify all lists' height
        /*justifyListViewHeightBasedOnChildren(lvCashInvestAssets);
        justifyListViewHeightBasedOnChildren(lvLongTermAssets);
        justifyListViewHeightBasedOnChildren(lvOtherAssets);
        justifyListViewHeightBasedOnChildren(lvShortTermLiabilities);
        justifyListViewHeightBasedOnChildren(lvLongTermDebts);*/
        // Save button on click
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Collect data save
                List<MoneyRecord> moneyRecords = CustomAdapter.fullMoneyRecords;
                List<Asset> assets = new ArrayList<>();
                List<Liability> liabilities = new ArrayList<>();
                for (int i = 0; i < moneyRecords.size(); i++) {
                    // Divide full money records to suitable assets and liabilities list (amount only)
                    // as required parameters to request to server
                    MoneyRecord moneyRecord = moneyRecords.get(i);
                    if (moneyRecord instanceof Asset) {
                        Asset asset = (Asset) moneyRecord;
                        assets.add(asset);
                    } else if (moneyRecord instanceof Liability) {
                        Liability liability = (Liability) moneyRecord;
                        liabilities.add(liability);
                    }
                }
                User user = new User(userName, assets, liabilities);
                // Save data to DB (Add if id == 0 and Update if id > 0)
                if (id == 0) {
                    new AddUserTask().execute(user);
                } else {
                    new UpdateUserTask().execute(user);
                }
            }
        });
    }

    class AddUserTask extends AsyncTask<User, Void, Long> {
        @Override
        protected Long doInBackground(User... users) {
            final User user = users[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                // Set up RestTemplate to call addUser API
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<User> requestEntity = new HttpEntity<>(user, requestHeaders);
                ResponseEntity<Long> responseEntity = restTemplate.exchange(USER_API_URL_EMULATOR, HttpMethod.POST, requestEntity, Long.class);
                return responseEntity.getBody();
            } catch (Exception ex) {
                return new Long(0);
            }
        }

        @Override
        protected void onPostExecute(Long userId) {
            // Update text views of total assets, total liabilities and net worth
            if (userId > 0) {
                Toast.makeText(NetWorthTrackingActivity.this, SAVE_DATA_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                //Utility.saveLongToSharePref(getApplicationContext(), SHARED_PREFERENCE_NAME, userName, userId);
                id = userId;
            } else {
                Toast.makeText(NetWorthTrackingActivity.this, DEFAULT_SERVER_EXCEPTION, Toast.LENGTH_LONG).show();
            }
        }
    }

    class UpdateUserTask extends AsyncTask<User, Void, Long> {
        @Override
        protected Long doInBackground(User... users) {
            final User user = users[0];
            RestTemplate restTemplate = new RestTemplate();
            try {
                // Set up RestTemplate to call addUser API
                String updateUserURL = String.format("%s%s%s", USER_API_URL_EMULATOR, "/", String.valueOf(id));
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<User> requestEntity = new HttpEntity<>(user, requestHeaders);
                ResponseEntity<Long> responseEntity = restTemplate.exchange(updateUserURL, HttpMethod.PUT, requestEntity, Long.class);
                return responseEntity.getBody();
            } catch (Exception ex) {
                return new Long(0);
            }
        }

        @Override
        protected void onPostExecute(Long userId) {
            // Update text views of total assets, total liabilities and net worth
            if (userId > 0) {
                Toast.makeText(NetWorthTrackingActivity.this, SAVE_DATA_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                //Utility.saveLongToSharePref(getApplicationContext(), SHARED_PREFERENCE_NAME, userName, userId);
                id = userId;
            } else {
                Toast.makeText(NetWorthTrackingActivity.this, DEFAULT_SERVER_EXCEPTION, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void prepareDefaultMoneyRecords() {
        sampleMoneyRecords = new ArrayList<>();
        // Add default cash assets
        sampleMoneyRecords.add(new Asset(CHEQUING, DEFAULT_AMOUNT, Asset.AssetType.CASH));
        sampleMoneyRecords.add(new Asset(SAVINGS_FOR_TAXS, DEFAULT_AMOUNT, Asset.AssetType.CASH));
        sampleMoneyRecords.add(new Asset(RAINY_DAY_FUND, DEFAULT_AMOUNT, Asset.AssetType.CASH));
        sampleMoneyRecords.add(new Asset(SAVINGS_FOR_FUN, DEFAULT_AMOUNT, Asset.AssetType.CASH));
        sampleMoneyRecords.add(new Asset(SAVINGS_FOR_TRAVEL, DEFAULT_AMOUNT, Asset.AssetType.CASH));
        sampleMoneyRecords.add(new Asset(SAVINGS_FOR_PERSONAL_DEVELOPMENT, DEFAULT_AMOUNT, Asset.AssetType.CASH));
        // Add default investment assets
        sampleMoneyRecords.add(new Asset(INVESTMENT_1, DEFAULT_AMOUNT, Asset.AssetType.INVESTMENT));
        sampleMoneyRecords.add(new Asset(INVESTMENT_2, DEFAULT_AMOUNT, Asset.AssetType.INVESTMENT));
        sampleMoneyRecords.add(new Asset(INVESTMENT_3, DEFAULT_AMOUNT, Asset.AssetType.INVESTMENT));
        sampleMoneyRecords.add(new Asset(INVESTMENT_4, DEFAULT_AMOUNT, Asset.AssetType.INVESTMENT));
        sampleMoneyRecords.add(new Asset(INVESTMENT_5, DEFAULT_AMOUNT, Asset.AssetType.INVESTMENT));
        // Add default long term assets
        sampleMoneyRecords.add(new Asset(PRIMARY_HOME, DEFAULT_AMOUNT, Asset.AssetType.LONG_TERM_ASSET));
        sampleMoneyRecords.add(new Asset(SECOND_HOME, DEFAULT_AMOUNT, Asset.AssetType.LONG_TERM_ASSET));
        // Add default other assets

        // Add default short term liabilities
        sampleMoneyRecords.add(new Liability(CREDIT_CARD_1, DEFAULT_AMOUNT, Liability.LiabilityType.SHORT_TERM_LIABILITY));
        sampleMoneyRecords.add(new Liability(CREDIT_CARD_2, DEFAULT_AMOUNT, Liability.LiabilityType.SHORT_TERM_LIABILITY));
        sampleMoneyRecords.add(new Liability(OTHERS, DEFAULT_AMOUNT, Liability.LiabilityType.SHORT_TERM_LIABILITY));
        // Add default long term debts
        sampleMoneyRecords.add(new Liability(MORtGAGE_1, DEFAULT_AMOUNT, Liability.LiabilityType.LONG_TERM_DEBT));
        sampleMoneyRecords.add(new Liability(MORtGAGE_2, DEFAULT_AMOUNT, Liability.LiabilityType.LONG_TERM_DEBT));
        sampleMoneyRecords.add(new Liability(LINE_OF_CREDIT, DEFAULT_AMOUNT, Liability.LiabilityType.LONG_TERM_DEBT));
        sampleMoneyRecords.add(new Liability(INVESTMENT_LOAN, DEFAULT_AMOUNT, Liability.LiabilityType.LONG_TERM_DEBT));
        sampleMoneyRecords.add(new Liability(STUDENT_LOAN, DEFAULT_AMOUNT, Liability.LiabilityType.LONG_TERM_DEBT));
        sampleMoneyRecords.add(new Liability(CAR_LOAN, DEFAULT_AMOUNT, Liability.LiabilityType.LONG_TERM_DEBT));
    }

    private void justifyListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        View listItem;
        int totalHeight = 0;
        //int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            listItem = adapter.getView(i, null, listView);
            //listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void updateTextViews(double totalAssets, double totalLiabilities, double netWorth) {
        // Update total assets Text View
        tvTotalAssets.setText(getDecimalFormat(totalAssets));
        // Update total liabilities Text View
        tvTotalLiabilities.setText(getDecimalFormat(totalLiabilities));
        // Update net worth Text View
        tvNetWorth.setText(getDecimalFormat(netWorth));
    }

    /*
    Add '.0' to string of integer format to make it decimal format
     */
    private String getDecimalFormat(double amount) {
        String formattedString = df.format(amount);
        if (!formattedString.contains(".")) {
            formattedString += ".0";
        }
        return formattedString;
    }
}
