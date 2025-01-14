import java.sql.SQLException;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class UserInterface {
    private static Scanner scanner = new Scanner(System.in);
    static int choice;
    Services test = new Services();


    public UserInterface() {

    }

    public void startMenu() {

        try {
            System.out.println("1: Logga in \n 2: Registrera dig \n 3: Fortsätt som gäst");
            choice = scanner.nextInt();

            if(choice == 1) {
                logIn();
            }

            else if (choice == 2) {
                register();
            }

            else if (choice == 3) {
                gästMenu();
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt val.");
        }
    }


    public void logIn() {
        System.out.println("1: Logga in som kund \n " +
                "2: Logga in som admin");
        choice = scanner.nextInt();

        if(choice == 1) {
            String Fname = getUserInput("Ange förnamn: ");
            String Lname = getUserInput("Ange efternamn: ");
            String password = getUserInput("Ange lösenord: ");
            int signedIn = Services.confirmLogInCustomer(Fname, Lname, password);
            if(signedIn == 0) {
                System.out.println("Du finns inte registrerad. ");
            }

            else if(signedIn == 1) {
                System.out.println("Inloggad");
                customerMenu();
            }
        }

        else if(choice == 2) {
            String Fname = getUserInput("Ange förnamn: ");
            String Lname = getUserInput("Ange efternamn: ");
            String password = getUserInput("Ange lösenord: ");
            int signed = Services.confirmLogInAdmin(Fname, Lname, password);

            if(signed == 0) {
                System.out.println("Du finns inte registrerad. ");
            }

            else if(signed == 1) {
                System.out.println("Inloggad");
                adminMenu();
            }
        }
    }

    public void register() {
        System.out.println("1: Registrera dig som kund \n " +
                "2: Registrera dig som admin");
        choice = scanner.nextInt();

        if(choice == 1) {
            String Fname = getUserInput("Ange förnamn: ");
            String Lname = getUserInput("Ange efternamn: ");
            String password = getUserInput("Ange ett lösenord: ");
            String email = getUserInput("Ange email: ");
            String adress = getUserInput("Ange address: ");
            String city = getUserInput("Ange stad: ");
            String country = getUserInput("Ange land: ");
            String phoneNumber = getUserInput("Ange phone number: ");
            int cID = Integer.parseInt(getUserInput("Vad vill du ha för ID?: "));
            test.registerCustomer(Fname, Lname, email, adress, city, country, phoneNumber, password, cID);
            customerMenu();
        }

        else if(choice == 2) {
            String Fname = getUserInput("Ange förnamn: ");
            String Lname = getUserInput("Ange efternamn: ");
            String password = getUserInput("Ange ett lösenord: ");
            test.registerAdmin(Fname, Lname, password);
            adminMenu();
        }
    }

    public void customerMenu() {

        try {
            Scanner scanner = new Scanner(System.in);

            boolean isRunning = true;

            while (isRunning) {
                System.out.println("Please select one of the following options: " +
                        " \n 1- Create a new order \n 2- View specific product " +
                        "\n 3- View all products" +
                        "\n 4- Add to order  " +
                        "\n 5- See shopping list and pay" +
                        "\n 6- Delete order" + "\n " +
                        "7- Exit \n Ange val:" );
                choice = scanner.nextInt();



                switch (choice) {
                    case 1:
                        int customer_id = Integer.parseInt(getUserInput("Ange din kund id: "));
                        test.created_new_order(customer_id);
                        break;

                    case 2:
                        int pCode = Integer.parseInt(getUserInput("Ange produktens kod: "));
                        String pName = getUserInput("Ange produktens namn: ");
                        String supplName = getUserInput("Ange leverantörens namn: ");
                        int supplID = Integer.parseInt(getUserInput("Ange leverantörens ID: "));
                        int price = Integer.parseInt(getUserInput("Ange produktens pris: "));
                        test.viewOfSpecifikProduct(pCode, pName, supplName, supplID, price);
                        break;

                    case 3:
                        test.view_all_products();
                        break;

                    case 4:
                        int order_id = Integer.parseInt(getUserInput("I vilken beställnings ID vill du lägga in produkten?: "));
                        String product_name = getUserInput("Ange produktens namn: ");
                        int product_code = Integer.parseInt(getUserInput("Ange produktens kod: "));
                        customer_id = Integer.parseInt(getUserInput(" Ange din egna kund ID: "));
                        int quantity_ordered = Integer.parseInt(getUserInput("Ange antal av produkten som du vill lägga till i varukorgen: "));
                        test.addToOrder(order_id, product_name, product_code, customer_id, quantity_ordered);
                        break;

                    case 5:
                        customer_id = Integer.parseInt(getUserInput("Ange din kund id: "));
                        test.view_of_orders(customer_id);
                        test.total_price(customer_id);
                        break;

                    case 6:
                        order_id = Integer.parseInt(getUserInput("Ange id för den order som ska raderas: "));
                        test.deleteOrder(order_id);
                        break;

                    case 7:
                        System.exit(0);
                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adminMenu() {

        try { // DETTA SKA VISAS FÖR ADMIN

           // Services test = new Services();
            Scanner scanner = new Scanner(System.in);

            boolean isRunning = true;

            while(isRunning){

                System.out.println("Please select one of the following options: \n 1- Add a list of suppliers \n 2- Add a product to stock" +
                        " \n 3- View all products \n " +
                        "4- Edit product quantity    " +
                        "\n 5- Delete product " +
                        "\n 6- Find product \n 7- Add discount \n " +
                        "8-View all discounts \n 9-Add discount to product \n 10-" +
                        " Change discount percentage \n 11-" +
                        " View discount history \n 12-" +
                        " Confirm orders \n 13- " +  //
                        " View non confirmed orders \n 14-" +
                        " See products with max orders in each month \n 15- see all discounts \n 16- Exit  \n Ange val: ");
                choice = scanner.nextInt();

                switch(choice) {
                    case 1:
                        String supplierName = getUserInput("Vad är namnet på leverantören?: ");
                        String phoneNUmber = getUserInput("Vad är telefonnumret?: ");
                        String address = getUserInput("Vad är addressen för leverantören?: ");
                        int supplierID = Integer.parseInt(getUserInput(" Vad är leverantörens ID?: "));
                        test.addSupplier(supplierName, phoneNUmber, address, supplierID);
                        break;

                    case 2:
                        String produktNamn = getUserInput("Vad heter produkten?: ");
                        int quantity = Integer.parseInt(getUserInput("Ange antal av produkten som ska finnas i lager: "));
                        int basePrice = Integer.parseInt(getUserInput("Vad är basPriset?"));
                        String supplierOfProduct = getUserInput("Ange namn av leverantören: ");
                        supplierID = Integer.parseInt(getUserInput(" Ange leverantörens id"));
                        int productCode = Integer.parseInt(getUserInput(" Ange produktens id"));
                        test.addProduct(produktNamn, quantity, basePrice, supplierOfProduct, supplierID, productCode);
                        break;

                    case 3:
                        test.view_all_products();
                        break;

                    case 4:
                        int product_code = Integer.parseInt(getUserInput(" Ange produktens id"));
                        produktNamn = getUserInput("Ange produktens namn: ");
                        int changeQuantity = Integer.parseInt(getUserInput(" Vad ska dess antal ändras till?:"));

                        test.editProductQuantity(changeQuantity, product_code, produktNamn);
                        break;

                    case 5:
                        product_code = Integer.parseInt(getUserInput("Ange produktens id som ska raderas: "));
                        produktNamn = getUserInput("Ange produktens namn: ");
                        test.deleteProduct(product_code, produktNamn);
                        break;

                    case 6:
                        product_code = Integer.parseInt(getUserInput(" Ange produktens id"));
                        produktNamn = getUserInput("Ange produktens namn: ");
                        supplierOfProduct = getUserInput("Ange namn av leverantören: ");
                        supplierID = Integer.parseInt(getUserInput(" Ange leverantörens id"));
                        test.findMyProduct(product_code, produktNamn, supplierOfProduct, supplierID);
                        break;

                    case 7:
                        String discount_name = getUserInput("Ange namn för rabatten: ");
                        double discount_percentage = Integer.parseInt(getUserInput("Ange procent för rabatten"));
                        String from_date = getUserInput("Ange start datumet för rabatten (YYYY-MM-DD):");
                        String to_date = getUserInput("Ange slut datumet för rabatten (YYYY-MM-DD):");
                        int discount_id = Integer.parseInt(getUserInput(" Ange rabattens id"));


                        test.add_discount(discount_name, discount_percentage, from_date, to_date, discount_id);
                        break;

                    case 8:
                        test.view_of_discounts();
                        break;

                    case 9:
                        product_code = Integer.parseInt(getUserInput(" Ange produktens id"));
                         produktNamn = getUserInput("Ange produktens namn: ");
                        discount_id = Integer.parseInt(getUserInput(" Ange rabattens id"));
                        test.add_discount_to_product(product_code, produktNamn, discount_id);
                        break;

                    case 10:
                        discount_id = Integer.parseInt(getUserInput("Ange rabattens ID: "));
                        discount_percentage = Integer.parseInt(getUserInput("Vad ska den nya procent vara?: "));
                        test.changeDiscountPerCentage(discount_id, discount_percentage);
                        break;

                    case 11:
                        test.discount_history();
                        break;

                    case 12:
                        int order_id = Integer.parseInt(getUserInput("Ange order id: "));
                        test.confirmed_order_by_admin(order_id);
                        break;

                    case 13:
                        test.view_non_confirmed_orders();
                        break;

                    case 14:
                        test.maximumOrderss();
                        break;

                    case 15:
                        test.view_of_discounts();

                    case 16:
                        System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void gästMenu() {

        try { // DETTA SKA VISAS FÖR ADMIN

            // Services test = new Services();
            Scanner scanner = new Scanner(System.in);

            boolean isRunning = true;

            while(isRunning){

                System.out.println("Please select one of the following options:"
                        + "\n 1- View all products" +
                        "\n 2- Find product " +
                        "\n 3- Exit  \n Ange val: ");
                choice = scanner.nextInt();

                switch(choice) {

                    case 1:
                        test.view_all_products();
                        break;

                        case 2:
                            int product_code = Integer.parseInt(getUserInput(" Ange produktens id"));
                            String produktNamn = getUserInput("Ange produktens namn: ");
                            String supplierOfProduct = getUserInput("Ange namn av leverantören: ");
                            int supplierID = Integer.parseInt(getUserInput(" Ange leverantörens id"));
                            test.findMyProduct(product_code, produktNamn, supplierOfProduct, supplierID);
                            break;

                    case 3:
                        System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


        /*
        try {

            Services test = new Services();


            System.out.println("Please select one \n 1- login as admin \n 2- login as customer");

            int userInput = Integer.parseInt(getUserInput(null));

            if (userInput == 1) {
                String username = getUserInput("Ange användarnamn: ");
                String password = getUserInput("Ange lösenord: ");
                String userType = getUserInput("Välj användartyp (admin eller customer): ");

                // Skapa användaren i databasen
                Services.createUser(username, password, userType);

                System.out.println("Användare skapad!");
            }

            else if (userInput == 2) {
                int deptId = Integer.parseInt(getUserInput("Which Department ID?"));
                //test.printEmployeesByDepartment(deptId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
         */

    public static String getUserInput(String msg) {
        Scanner myObj = new Scanner(System.in);
        if (msg != null)
            System.out.println(msg);
        String input = myObj.nextLine();
        return input;
    }
}