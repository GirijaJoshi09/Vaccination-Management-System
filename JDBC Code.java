import java.util.*;
import java.sql.*;
import java.sql.Date;

public class DBMS_MINIPROJECT {
	private static final String URL = "jdbc:mysql://localhost:3306/miniproject";
	private static final String USER = "root";
	private static final String PASSWORD = "Girija@1234";
	private static Connection conn;
	private static String phone_no = "";
	private static int ben_id = 0;
	private static int vaccineId = 0;
	private static int rem_id = 0;
	private static String reminderDate = "";
	private final Scanner scanner = new Scanner(System.in);

	public DBMS_MINIPROJECT() {
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addUser(String phone_no, String password) throws SQLException {
		String query = "INSERT INTO User (phone_no,password) VALUES (?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, phone_no);
			stmt.setString(2, password);
			stmt.executeUpdate();
			System.out.println("User signed up successfully.");
		}
	}

	public static void getUsers() throws SQLException {
		String query = "SELECT * FROM User";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("Phone number: " + rs.getString("phone_no"));
				System.out.println("Password: " + rs.getString("password"));
				System.out.println("-----------");
			}
		}
	}

	public static void login() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter Phone number:");
		phone_no = sc.next();
		sc.nextLine(); // Consume newline

		System.out.println("Enter Password:");
		String password = sc.nextLine();
		String sql = "SELECT * FROM User WHERE phone_no = ? AND password = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, phone_no);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				System.out.println("Login successful! Welcome!! ");
			} else {
				System.out.println("Invalid User ID or Name.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addBeneficiary(int id, String name, String dob, String gender, long phone) throws SQLException {
		String query = "INSERT INTO benificiary (benf_id, benf_name, date_of_birth, gender, phone_no) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setDate(3, java.sql.Date.valueOf(dob)); // Convert String to Date
			stmt.setString(4, gender);
			stmt.setLong(5, phone);
			int rowsInserted = stmt.executeUpdate();
			System.out.println(rowsInserted > 0 ? "Beneficiary added successfully!" : "Failed to add beneficiary.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void displayBeneficiaries() throws SQLException {
		String query = "select * from benificiary natural join user where phone_no = ? ";
		// String query = "select * from benificiary";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, phone_no);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
				System.out.println("No beneficiaries found for this user.");
			} else {
				while (rs.next()) {
					// System.out.println("Name : " +rs.getString("benf_name"));
					System.out.println("ID:" + rs.getInt("benf_id") + " Name: " + rs.getString("benf_name") + " DOB: "
							+ rs.getDate("date_of_birth") + " Gender: " + rs.getString("gender") + " Phone: "
							+ rs.getString("phone_no"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteBeneficiary() {
		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter the benificiary id you want to delete : ");
		int benfi_id = sc.nextInt();
		String query = "DELETE FROM benificiary WHERE benf_id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			// Set the beneficiary ID in the query
			stmt.setInt(1, benfi_id);

			// Execute the delete statement
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Beneficiary with ID " + benfi_id + " has been successfully deleted.");
			} else {
				System.out.println("No beneficiary found with ID " + ben_id + ".");
			}

		} catch (SQLException e) {
			System.out.println("Error while deleting beneficiary.");
			e.printStackTrace();
		}
	}

	public static void updateBeneficiary() {
		String query = "UPDATE vaccine_record SET status = 'completed' WHERE benf_id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			// Set the parameters for the update query

			stmt.setInt(1, ben_id);

			// Execute the update statement
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Beneficiary with ID " + ben_id + " has been successfully updated.");
			} else {
				System.out.println("No beneficiary found with ID " + ben_id + ".");
			}

		} catch (SQLException e) {
			System.out.println("Error while updating beneficiary.");
			e.printStackTrace();
		}
	}

	public static void displayBeneficiariesPending() throws SQLException {
		String query = "select record_id,benf_id,vaccine_id,status from vaccine_record natural join user where status = 'pending' and phone_no = ? ";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, phone_no);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
				System.out.println("No beneficiaries found for this user.");
			} else {
				while (rs.next()) {
					
					System.out
							.println("Record ID:" + rs.getInt("record_id") + " Beneficiary ID: " + rs.getInt("benf_id")
									+ " Vaccine ID: " + rs.getInt("vaccine_id") + " Status: " + rs.getString("status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void displayBeneficiariesCompleted() throws SQLException {
		String query = "select record_id,benf_id,vaccine_id,status from vaccine_record natural join user where status = 'completed' and phone_no = ? ";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, phone_no);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
				System.out.println("No beneficiaries found for this user.");
			} else {
				while (rs.next()) {
					// System.out.println("Name : " +rs.getString("benf_name"));
					System.out
							.println("Record ID:" + rs.getInt("record_id") + " Beneficiary ID: " + rs.getInt("benf_id")
									+ " Vaccine ID: " + rs.getInt("vaccine_id") + " Status: " + rs.getString("status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void displayBeneficiariesAge() throws SQLException {
		String query = " CALL GetApplicableVaccines(?) ";
		try (CallableStatement stmt = conn.prepareCall(query)) {
			// Set the input parameter (benf_id)
			stmt.setInt(1, ben_id);

			// Execute the stored procedure
			ResultSet rs = stmt.executeQuery();

			// Process the result set
			System.out.println("Vaccines applicable to beneficiary (benf_id: " + ben_id + "):");
			System.out.printf("%-40s | %-50s | %-10s%n", "Vaccine", "Description", "Age Group");
			System.out.println("--------------------------------------------------------------------------");

			while (rs.next()) {
				String vaccineName = rs.getString("vaccine_name");
				String description = rs.getString("description");
				String ageGroup = rs.getString("age_group");

				// Print in tabular format
				System.out.printf("%-40s | %-50s | %-10s%n", vaccineName, description, ageGroup);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void displayEachBeneficiariesPending() throws SQLException {
		String query = "SELECT distinct benf_id, vaccine_id, status FROM vaccine_record NATURAL JOIN user WHERE status = 'pending' AND benf_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, ben_id); // Set ben_id for the prepared statement
			ResultSet rs = pstmt.executeQuery();

			if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
				System.out.println("No pending vaccines found for beneficiary ID: " + ben_id);
			} else {
				while (rs.next()) {
					System.out.println("Beneficiary ID: " + rs.getInt("benf_id") + ", Vaccine ID: "
							+ rs.getInt("vaccine_id") + ", Status: " + rs.getString("status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void displayEachBeneficiariesCompleted() throws SQLException {
		String query = "SELECT distinct benf_id, vaccine_id, status FROM vaccine_record NATURAL JOIN user WHERE status = 'completed' AND benf_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, ben_id); // Set ben_id for the prepared statement
			ResultSet rs = pstmt.executeQuery();

			if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
				System.out.println("No completed vaccines found for beneficiary ID: " + ben_id);
			} else {
				while (rs.next()) {
					System.out.println("Beneficiary ID: " + rs.getInt("benf_id") + ", Vaccine ID: "
							+ rs.getInt("vaccine_id") + ", Status: " + rs.getString("status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void addReminder() throws SQLException {
		String SQL = "INSERT INTO reminders (reminder_id, benf_id, vaccine_id, reminder_date) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			// Validate that the date is not empty or null
			if (reminderDate == null || reminderDate.isEmpty()) {
				//System.out.println("Reminder date is empty or null. Please provide a valid date in the format YYYY-MM-DD.");
				System.out.println("");
				return;
			}

			// Validate date format and convert to java.sql.Date
			java.sql.Date sqlDate;
			try {
				sqlDate = java.sql.Date.valueOf(reminderDate); // Convert reminderDate to java.sql.Date
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid date format. Please ensure the date is in YYYY-MM-DD format.");
				return;
			}

			pstmt.setInt(1, rem_id); // reminder_id
			pstmt.setInt(2, ben_id); // benf_id
			pstmt.setInt(3, vaccineId); // vaccine_id
			pstmt.setDate(4, sqlDate); // reminder_date

			// Execute the insert statement
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Reminder inserted successfully! Trigger should update the vaccine record.");
			} else {
				System.out.println("Failed to insert reminder.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) throws SQLException {
		DBMS_MINIPROJECT project = new DBMS_MINIPROJECT();
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n** Vaccine Management System **");
		System.out.println("1. Sign Up");
		System.out.println("2. Log In");
		System.out.print("Choose an option: ");
		int choice = scanner.nextInt();
		boolean running = true;
		String phone_no = "";
		// String reminderDate = "";
		switch (choice) {
		case 1:
			System.out.print("Enter Phone number : ");
			phone_no = scanner.next();
			System.out.print("Enter Password: ");
			String password = scanner.next();
			addUser(phone_no, password);
			System.out.print("\nSign up successful !!");
			System.out.println("\nKindly login: ");
			login();
			while (true) {
				System.out.println("\n** Vaccine Management System **");

				System.out.println("1. Add Beneficiary");
				System.out.println("2. Display Beneficiaries"); // show vaccines acc to age grp
				System.out.println("3. Delete Beneficiaries");
				// view pending vaccines of all benf.
				System.out.println("4.Show pending vaccines  of all benificiary:");
				System.out.println("5.Show completed vaccines  of all benificiary:");
				System.out.println("6.Add Reminder to pending vaccines :");
				System.out.println("7. Exit");
				System.out.print("Enter your choice: ");
				int choice1 = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				try {
					switch (choice1) {

					case 1:
						System.out.print("Enter Beneficiary ID: ");
						int benfId = scanner.nextInt();
						scanner.nextLine(); // Consume newline
						System.out.print("Enter Beneficiary Name: ");
						String benfName = scanner.nextLine();
						System.out.print("Enter date of birth (YYYY-MM-DD): ");
						String benfDob = scanner.nextLine();
						System.out.print("Enter gender (M/F) : ");
						String benfGender = scanner.nextLine();
						System.out.print("Enter phone no. used for login : ");
						long phone = scanner.nextLong();
						addBeneficiary(benfId, benfName, benfDob, benfGender, phone);
						displayBeneficiaries();
						break;
					case 2:
						displayBeneficiaries();

						System.out.print("\nEnter the beneficiary id of the child whose details you want to see : ");
						ben_id = scanner.nextInt();
						do {
							System.out.print(
									"\n1:Show all vaccines according to his/her age group\n2:Show all pending vaccines\n3:Show vaccine history\n4:Add reminders\n5:Update its status to completed\n6:Exit");
							System.out.print("\nEnter your choice : ");
							int ch = scanner.nextInt();
							switch (ch) {
							case 1:

								displayBeneficiariesAge();
								break;

							case 2:

								displayEachBeneficiariesPending();
								break;

							case 3:

								displayEachBeneficiariesCompleted();
								break;

							case 4:

								System.out.print("Enter Vaccine ID: ");
								int reminderVaccineId = scanner.nextInt();
								scanner.nextLine(); // Consume newline
								System.out.print("Enter Beneficiary ID: ");
								int beneUserId = scanner.nextInt();
								System.out.print("Enter Reminder ID: ");
								int remId = scanner.nextInt();
								
								 System.out.print("Enter reminder date (YYYY-MM-DD): ");
								 String rem_Date =scanner.nextLine();
								 
								addReminder();
								System.out.println("Reminder added successfully!");

								break;
						
							case 5:
								// update
								updateBeneficiary();
								break;
							case 6:
								System.out.println("Exiting the Vaccine Management System.");
								running = false;
								break;

							default:
								System.out.println("Invalid choice. Please enter a valid option.");

							}
							// scanner.close();

						} while (running);
						break;
					case 3:
						// delete
						deleteBeneficiary();
						displayBeneficiaries();
						break;
					case 4:
						displayBeneficiariesPending();
						break;
					case 5:
						displayBeneficiariesCompleted();
						break;
					case 6:

						System.out.print("Enter Vaccine ID: ");
						vaccineId = scanner.nextInt();
						scanner.nextLine(); // Consume newline
						System.out.print("Enter Beneficiary ID: ");
						ben_id = scanner.nextInt();
						System.out.print("Enter Reminder ID: ");
						rem_id = scanner.nextInt();
						System.out.print("Enter reminder date (YYYY-MM-DD): ");
						String rem_Date = scanner.nextLine();
						addReminder();
						System.out.println("Reminder added successfully!");
						break;
					case 7:
						System.out.println("Exiting the Vaccine Management System.");
						// scanner.close();
						return;
					default:
						System.out.println("Invalid choice. Please enter a valid option.");
						break;

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		case 2:
			login();
			while (true) {
				System.out.println("\n** Vaccine Management System **");

				System.out.println("1. Add Beneficiary");
				System.out.println("2. Display Beneficiaries"); // show vaccines acc to age grp

				// view pending vaccines of all benf.
				System.out.println("3. Delete Beneficiaries");
				System.out.println("4.Show pending vaccines of all benificiary:");
				System.out.println("5.Show completed vaccines of all benificiary:");
				System.out.println("6.Add Reminder to pending vaccines :");
				System.out.println("7. Exit");
				System.out.print("Enter your choice: ");
				int choice1 = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				try {
					switch (choice1) {

					case 1:
						System.out.print("Enter Beneficiary ID: ");
						int benfId = scanner.nextInt();
						scanner.nextLine(); // Consume newline
						System.out.print("Enter Beneficiary Name: ");
						String benfName = scanner.nextLine();
						System.out.print("Enter date of birth (YYYY-MM-DD): ");
						String benfDob = scanner.nextLine();
						System.out.print("Enter gender (M/F) : ");
						String benfGender = scanner.nextLine();
						System.out.print("Enter phone no. used for login : ");
						long phone = scanner.nextLong();
						addBeneficiary(benfId, benfName, benfDob, benfGender, phone);
						displayBeneficiaries();
						break;
					case 2:
						displayBeneficiaries();

						System.out.print("\nEnter the beneficiary id of the child whose details you want to see : ");
						ben_id = scanner.nextInt();
						do {
							System.out.print(
									"\n1:Show all vaccines according to his/her age group\n2:Show all pending vaccines\n3:Show vaccine history\n4:Add reminders\n5:Update its status to completed\n6:Exit");
							System.out.print("\nEnter your choice : ");
							int ch = scanner.nextInt();
							switch (ch) {
							case 1:

								displayBeneficiariesAge();
								break;

							case 2:

								displayEachBeneficiariesPending();
								break;

							case 3:

								displayEachBeneficiariesCompleted();
								break;

							case 4:
								//System.out.println("HIIIIIII 4");
								System.out.print("Enter Vaccine ID: ");
								int reminderVaccineId = scanner.nextInt();
								scanner.nextLine(); // Consume newline
								System.out.print("Enter Beneficiary ID: ");
								int beneUserId = scanner.nextInt();
								System.out.print("Enter Reminder ID: ");
								int remId = scanner.nextInt();
							
								  System.out.print("Enter reminder date (YYYY-MM-DD): "); 
								  String rem_Date = scanner.nextLine();
								 
								 
								addReminder();
								System.out.println("Reminder added successfully!");
								break;
					
							case 5:
								// update
								updateBeneficiary();
								break;
							case 6:
								System.out.println("Exiting the Vaccine Management System.");
								running = false;
								break;

							default:
								System.out.println("Invalid choice. Please enter a valid option.");

							}
							// scanner.close();

						} while (running);
						break;
					case 3:
						// delete;
						
						deleteBeneficiary();
						displayBeneficiaries();
						break;
					case 4:

						displayBeneficiariesPending();
						break;

					case 5:
						displayBeneficiariesCompleted();
						break;

					case 6:

						System.out.print("Enter Vaccine ID: ");
						int reminderVaccineId = scanner.nextInt();
						scanner.nextLine(); // Consume newline
						System.out.print("Enter Beneficiary ID: ");
						int beneUserId = scanner.nextInt();
						System.out.print("Enter Reminder ID: ");
						int remId = scanner.nextInt();
						System.out.print("Enter reminder date (YYYY-MM-DD): ");
						 String rem_Date = scanner.nextLine();
						addReminder();
						System.out.println("Reminder added successfully!");
						break;

					case 7:
						System.out.println("Exiting the Vaccine Management System.");
						// scanner.close();
						return;

					default:
						System.out.println("Invalid choice. Please enter a valid option.");
						break;

					}
				} catch (SQLException e) {
					e.printStackTrace();

					// scanner.close();
				}
			}
		}
	}
}
