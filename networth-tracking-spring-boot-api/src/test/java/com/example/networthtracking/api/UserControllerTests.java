package com.example.networthtracking.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.example.networthtracking.model.Asset;
import com.example.networthtracking.model.Liability;
import com.example.networthtracking.model.MoneyRecord;
import com.example.networthtracking.model.User;

public class UserControllerTests {
	private static RestTemplate restTemplate;
	private static User user;
	private static HttpHeaders requestHeaders;
	final String USER_API_URL = "http://localhost:8080/user";
	final String DEFAULT_NAME = "Danny";
	final String CHEQUING = "Chequing";
	final String INVESTMENT_1 = "Investment 1";
	final String PRIMARY_HOME = "Primary Home";
	final String CREDIT_CARD_1 = "Credit Card 1";
	final String STUDENT_LOAN = "Student Loan";

	@BeforeAll
	private static void init() {
		restTemplate = new RestTemplate();
		user = new User();
		requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testAddUserWithValidUserInfo() {
		// Call getAllUsers API before adding to check the number of users in DB
		List<User> usersBeforeAdding = callGetAllUsersAPI();
		int userListSizeBeforeAdd = usersBeforeAdding.size();
		// Construct a user object to add to DB
		user = constructSampleUser();
		// Set up RestTemplate to call addUser API
		HttpEntity<User> requestEntity = new HttpEntity<>(user, requestHeaders);
		ResponseEntity<Long> responseEntity = restTemplate.exchange(USER_API_URL, HttpMethod.POST, requestEntity, Long.class);
		// Test the response for adding a user
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		// Call getAllUsers API after adding to test if the number of users in DB is
		// increased by 1
		List<User> usersAfterAdding = callGetAllUsersAPI();
		int userListSizeAfterAdd = usersAfterAdding.size();
		assertEquals(userListSizeAfterAdd, userListSizeBeforeAdd + 1);
		// Get userId of added user
		long userId = responseEntity.getBody();
		// Test that added user has not exist in DB before adding
		Optional<User> optUser = usersBeforeAdding.stream().filter(user -> user.getId() == userId).findFirst();
		assertEquals(Optional.empty(), optUser);
		// Test that added user exists in DB after adding
		optUser = usersAfterAdding.stream().filter(user -> user.getId() == userId).findFirst();
		User addedUser = optUser.get();
		assertNotNull(addedUser);
		// Test added user's values vs the constructed user's to add
		assertEquals(DEFAULT_NAME, addedUser.getName());
		testAddedMoneyRecords(user.getAssets(), addedUser.getAssets());
		testAddedMoneyRecords(user.getLiabilities(), addedUser.getLiabilities());
	}

	private User constructSampleUser() {
		List<Asset> assets = Arrays.asList(new Asset(CHEQUING, 2000D, Asset.AssetType.CASH),
				new Asset(INVESTMENT_1, 5000D, Asset.AssetType.INVESTMENT),
				new Asset(PRIMARY_HOME, 500000D, Asset.AssetType.LONG_TERM_ASSET));
		List<Liability> liabilities = Arrays.asList(
				new Liability(CREDIT_CARD_1, 2000D, Liability.LiabilityType.SHORT_TERM_LIABILITY),
				new Liability(STUDENT_LOAN, 10000D, Liability.LiabilityType.LONG_TERM_DEBT));
		user.setName(DEFAULT_NAME);
		user.setAssets(assets);
		user.setLiabilities(liabilities);
		return user;
	}

	private List<User> callGetAllUsersAPI() {
		User[] users = restTemplate.getForObject(USER_API_URL, User[].class);
		assertNotNull(users);
		return Arrays.asList(users);
	}
	
	/*
	 * Test 2 MoneyRecord list (constructed and added ones)
	 * They are equal if their sizes are equal and all of their element
	 * are equal
	 */
	private void testAddedMoneyRecords(List<? extends MoneyRecord> constructedList, List<? extends MoneyRecord> addedList) {
		assertNotNull(addedList);
		assertEquals(constructedList.size(), addedList.size());
		for (int i = 0; i < constructedList.size(); i++) {
			constructedList.get(i).equals(addedList.get(i));
		}
	}

	@AfterAll
	private static void destroy() {
		restTemplate = null;
		user = null;
		requestHeaders = null;
	}
}
