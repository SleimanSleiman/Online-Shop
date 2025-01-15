import java.sql.*;
import java.util.Scanner;

import  java.sql.Date;

public class Services {
    public Services() {

    }


    public Connection getDatabaseConnection() {
        String url = "jdbc:postgresql://pgserver.mau.se:5432/ao7408";
        String user = "ao7408";
        String password = "8ixdmx0u";
        //String user1 = "admin";
        //String user2 = "customer";

        //String url = "jdbc:postgresql://localhost/hr_ht23";
        //Properties props = new Properties();
        //props.setProperty("user", "johan_ht23");
        //props.setProperty("password", "12345");

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");
            return con;

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }


    public static void createUser(String username, String password, String userType) {
        try (Connection con = DriverManager.getConnection(username, password, userType)) {
            String insertQuery = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, userType);
                preparedStatement.executeUpdate();
            }
            System.out.println("Användare skapad.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int confirmLogInCustomer(String Fname, String Lname, String password) {
        int number = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

             CallableStatement callableStatement = con.prepareCall("{ ? = call confirmLoginCustomer(?, ?, ?)}")) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, Fname);
            callableStatement.setString(3, Lname);
            callableStatement.setString(4, password);

            callableStatement.executeUpdate();
            number = callableStatement.getInt(1);
            callableStatement.close();
            con.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static int confirmLogInAdmin(String Fname, String Lname, String password) {
        int number = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

             CallableStatement callableStatement = con.prepareCall("{ ? = call confirmLoginAdmin(?, ?, ?) }")) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, Fname);
            callableStatement.setString(3, Lname);
            callableStatement.setString(4, password);
            callableStatement.execute();

            number = callableStatement.getInt(1);

            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void printAllSuppliers() throws Exception {

        Connection con = getDatabaseConnection();

        String QUERY = "SELECT * FROM \"supplier\"";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY);
        while (rs.next()) {
            System.out.print("ID: " + rs.getInt("id"));
            System.out.print(", first name: " + rs.getString("first_name"));
            System.out.print(", Phone number " + rs.getString("phone_number"));
            System.out.println(", Address: " + rs.getInt("address"));

            System.out.println("----------------------------------------------------------");
        }

        stmt.close();
        con.close();
    }

    public void addSupplier(String suppliername, String phonenumber, String address, int supplier_id) throws Exception {
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

             CallableStatement callableStatement = con.prepareCall("call addSupplier(?, ?, ?, ?)")) {
            callableStatement.setString(1, suppliername);
            callableStatement.setString(2, phonenumber);
            callableStatement.setString(3, address);
            callableStatement.setInt(4, supplier_id);
            callableStatement.executeUpdate();
            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(String productname, int quantity, int basePrice, String supplier_of_product, int id_of_supplier, int productCode) throws Exception {
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             CallableStatement callableStatement = con.prepareCall("CALL addProduct(?, ?, ?, ?, ?, ?)")) {

            callableStatement.setString(1, productname);
            callableStatement.setInt(2, quantity);
            callableStatement.setInt(3, basePrice);
            callableStatement.setString(4, supplier_of_product);
            callableStatement.setInt(5, id_of_supplier);
            callableStatement.setInt(6, productCode);
            callableStatement.executeUpdate();
            System.out.print("Produkt vid namn: " + productname + " har lagts till!");

            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public void editProductQuantity(int changeQuantity, int productCode, String product_name) throws Exception {

        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

        CallableStatement callableStatement = con.prepareCall("call editProductQuantity(?,?,?)");
        callableStatement.setInt(1, productCode);
        callableStatement.setString(2, product_name);
        callableStatement.setInt(3, changeQuantity);
        callableStatement.executeUpdate();

        System.out.println("Antal av produkten har ändrats!");
        callableStatement.close();
        con.close();
    }


    public void deleteProduct(int productCode, String product_name) throws Exception {
        int number = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

             CallableStatement callableStatement = con.prepareCall("{ ? = call deleteProduct(?, ?)}")) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2, productCode);
            callableStatement.setString(3, product_name);

            callableStatement.executeUpdate();
            number = callableStatement.getInt(1);

            if (number == 1) {
                System.out.println("Product with the code: " + productCode + " and the name " + product_name + " is deleted successfully");

            } else {
                System.out.println("Product could not be deleted");
            }
            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add_discount(String discount_name, double discount_percentage, String from_date, String to_date, int discount_id) throws Exception {
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             CallableStatement callableStatement = con.prepareCall("call add_discount(?,?,?,?,?)")) {

            java.sql.Date fromDate = java.sql.Date.valueOf(from_date);
            java.sql.Date toDate = java.sql.Date.valueOf(to_date);

            callableStatement.setInt(1, discount_id);
            callableStatement.setString(2, discount_name);
            callableStatement.setDouble(3, discount_percentage);
            callableStatement.setDate(4, fromDate);
            callableStatement.setDate(5, toDate);
            callableStatement.executeUpdate();
            System.out.print("Rabatt vid namn " + discount_name + " med id " + discount_id +" har lagts till!");

            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void deleteOrder(int order_id) {
        int number = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

             CallableStatement callableStatement = con.prepareCall("{ ? = call delete_order(?) }")) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2, order_id);

            callableStatement.executeUpdate();
            number = callableStatement.getInt(1);

            if (number == 1) {
                System.out.println("Beställning har tagits bort!");
            } else {
                System.out.println("Beställning har inte tagits bort då den redan är bekräftad");
            }
            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void accepted_payment(int customer_id) {

        try {
            Connection con = getDatabaseConnection();
            PreparedStatement pstmt = con.prepareStatement("call accepted_payment(?)");
            pstmt.setInt(1, customer_id);
            pstmt.execute();
            System.out.println("Betalt!");
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void total_price(int customer_id) { //throws exception
        Scanner scanner = new Scanner(System.in);
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
            String query = "{ ? = call list_total_price(?)}";
            CallableStatement callableStatement = con.prepareCall(query);

            callableStatement.registerOutParameter(1, Types.INTEGER);

            callableStatement.setInt(2, customer_id);
            callableStatement.execute();

            int price = callableStatement.getInt(1);

            System.out.println("Det totala priset är: " + price + " kr");
            System.out.println("Klicka 1 för att betala: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                accepted_payment(customer_id);
            }

            callableStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }








    public void addToOrder(int order_id, String product_name, int product_code, int customer_id, int quantity_ordered) {
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             CallableStatement callableStatement = con.prepareCall("{ ? = call add_to_order( ?,?,?,?,?) }")) {

            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2, order_id);
            callableStatement.setString(3, product_name);
            callableStatement.setInt(4, product_code);
            callableStatement.setInt(5, customer_id);
            callableStatement.setInt(6, quantity_ordered);
            callableStatement.executeUpdate();

            int result = callableStatement.getInt(1);

            if (result == 1) {
                System.out.println("Produkt vid namn: " + product_name + " har lagts till!");
            } else {
                System.out.println("Produkt kunde inte läggas till");
            }


            callableStatement.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void view_all_products() throws Exception {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

            Statement statement = conn.createStatement();
            String view = "select * from view_of_products";
            ResultSet rs = statement.executeQuery(view);

            while (rs.next()) {
                int code = rs.getInt("code");
                String product_name = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                int basePrice = rs.getInt("basePrice");
                int discounted_price = rs.getInt("discounted_price");
                String supplier_of_product = rs.getString("supplier_of_product");
                int id_of_supplier = rs.getInt("id_of_supplier");
                int discount_id = rs.getInt("discount_id");
                int amount_sold = rs.getInt("amount_sold");
                System.out.println("Product code: " + code + " | Product name" + product_name + " | Quantity_left" + quantity + " | basePrice: " + basePrice +
                        " | Discounted_price: " + discounted_price + " | Supplied by: " + supplier_of_product + " | Supplier ID: " + id_of_supplier +
                        " | " + discount_id + " | " + amount_sold);

                System.out.println("----------------------------------------------------------");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewOfSpecifikProduct(int code, String name, String supplier, int supplier_id, int price) throws Exception {
        String query = "SELECT * FROM view_of_specific_products(?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, code);
            stmt.setString(2, name);
            stmt.setString(3, supplier);
            stmt.setInt(4, supplier_id);
            stmt.setInt(5, price);

            try (ResultSet rs = stmt.executeQuery()) {
                while ((rs.next())) {
                    code = rs.getInt("produktKodd");
                    name = rs.getString("namee");
                    supplier = rs.getString("supplierNamnn");
                    supplier_id = rs.getInt("supplierIDD");
                    price = rs.getInt("pricee");

                    System.out.println("Product code: " + code + " | Product name: " + name + " | Supplied by: " + supplier + " | Supplier ID: " + supplier_id + " | basePrice: " + price);
                }
            }
        }
    }

    public void view_of_discounts() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

            Statement statement = conn.createStatement();    //lagt till denna under funktioner.
            String view = "SELECT * FROM view_of_discounts";
            ResultSet rs = statement.executeQuery(view);

            while (rs.next()) {
                int discount_id = rs.getInt("discount_id");
                String discount_name = rs.getString("discount_name");
                double discount_percentage = rs.getDouble("discount_percentage");
                Date discount_from = rs.getDate("discount_from");
                Date discount_to = rs.getDate("discount_to");

                System.out.println("Discount ID: " + discount_id + " | Discount name: " + discount_name + " | Discount percentage: " + discount_percentage + " | From: " + discount_from +
                        " | To: " + discount_to);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void add_discount_to_product(int code, String product_name, int discount_id) throws Exception {

        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             CallableStatement callableStatement = con.prepareCall("CALL add_discount_to_product (?,?,?)")) {
            callableStatement.setInt(1, code);
            callableStatement.setString(2, product_name);
            callableStatement.setInt(3, discount_id);
            callableStatement.executeUpdate();
            System.out.println("Produkt har fått rabatt!");
            callableStatement.close();
            con.close();
        }
    }

    public void changeDiscountPerCentage(int discount_id, double discount_percentage) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

        CallableStatement callableStatement = con.prepareCall("call changeDiscountPerCentage(?,?)");
        callableStatement.setInt(1, discount_id);
        callableStatement.setDouble(2, discount_percentage);
        callableStatement.executeUpdate();

        System.out.println("Procent på rabatt har ändrats!");
        callableStatement.close();
        con.close();

    }

    public void see_discount_history() throws Exception {

        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
        String sqlQuery = "SELECT * FROM see_discount_history";
        try (Statement stmt = con.createStatement();
             ResultSet resultSet = stmt.executeQuery(sqlQuery)) {
            while (resultSet.next()) {
                int discountId = resultSet.getInt("discount_id");
                String discountName = resultSet.getString("discount_name");

                System.out.println("Discount ID: " + discountId + ", Discount Name: " + discountName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void confirmed_order_by_admin(int order_id) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

        CallableStatement callableStatement = con.prepareCall("call confirmed_order_by_admin(?)");
        callableStatement.setInt(1, order_id);
        callableStatement.executeUpdate();
        System.out.println("Beställning bekräftat!");
        callableStatement.close();
        con.close();

    }

    public void view_non_confirmed_orders() throws Exception {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

            Statement statement = conn.createStatement();
            String view = "select * from view_non_confirmed_orders";
            ResultSet rs = statement.executeQuery(view);

            while (rs.next()) {
                int order_id = rs.getInt("order_id");
                int customer_id = rs.getInt("customer_id");
                boolean confirmed = rs.getBoolean("confirmed");
                boolean paid = rs.getBoolean("paid");
                int fullprice = rs.getInt("fullprice");
                Date date_ordered = rs.getDate("date_ordered");

                System.out.println("Order ID: " + order_id + " | Customer ID: " + customer_id + " | Confirmed: " + confirmed + " | Paid: " + paid + " | Fullprice: " + fullprice + " | Date_ordered: " + date_ordered);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void maximum_orders() throws Exception {
        String query = "SELECT * FROM maximum_orders()";
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             PreparedStatement stmt = con.prepareStatement(query)) {

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    int quantity_ordered = rs.getInt("total_sales"); //klar
                    int product_code = rs.getInt("product_code"); //klar
                    String product_name = rs.getString("product_name"); //klar
                    Date month = rs.getDate("month"); //klar

                    System.out.println(month + " | " + product_code + " | " + product_name + " | " + quantity_ordered);
                }
            }
        }
    }

    public void registerCustomer(String first_name, String last_name, String email, String address, String city, String country, String phone_number, String password, int customer_id) {
        try {
            Connection con = getDatabaseConnection();
            PreparedStatement pstmt = con.prepareStatement("call registerCustomer(?,?,?,?,?,?,?,?, ?)");
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.setString(5, city);
            pstmt.setString(6, country);
            pstmt.setString(7, phone_number);
            pstmt.setString(8, password);
            pstmt.setInt(9, customer_id);
            pstmt.execute();
            pstmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerAdmin(String first_name, String last_name, String password) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

            PreparedStatement pstmt = con.prepareStatement("call registerAdmin(?,?,?)");
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, password);
            pstmt.execute();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void findTheProduct(int code, String product_name, String supplier_of_product, int supplier_Id) throws Exception {
        String query = "{ SELECT * FROM findProduct(?, ?, ?, ?, ?) }";

        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             Statement statement = con.createStatement();
             CallableStatement callableStatement = con.prepareCall(query)) {
            callableStatement.registerOutParameter(5, Types.INTEGER);

            callableStatement.setInt(1, code);
            // pstmt.setInt(2, code);
            callableStatement.setString(2, product_name);
            callableStatement.setString(3, supplier_of_product);
            callableStatement.setInt(4, supplier_Id);
            callableStatement.execute();
            int result1 = callableStatement.getInt(2);
            System.out.println(result1 + " --");

            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            while (rs.next()) {
                int pCode = rs.getInt("code");
                String pName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                int basePrice = rs.getInt("basePrice");
                int discounted_price = rs.getInt("discounted_price");
                String supplProduct = rs.getString("supplier_of_product");
                int supplID = rs.getInt("supplier_id");
                int discount_id = rs.getInt("discount_id");
                int amount_sold = rs.getInt("amount_sold");

                System.out.println("Product code: " + pCode + " | Product name: " + pName + " | Quantity: " + quantity + " | BasePrice: " + basePrice + " | Discounted_price: " + discounted_price
                        + " | Supplied by: " + supplProduct + " | Supplier ID: " + supplID + " | Discount ID: " + discount_id + " | Amount sold: " + amount_sold);
            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void findMyProduct(int code, String product_name, String supplier_of_product, int supplier_Id) throws Exception {
        String query = "SELECT * FROM findProduct(?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, code);
            stmt.setString(2, product_name);
            stmt.setString(3, supplier_of_product);
            stmt.setInt(4, supplier_Id);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    int Pcode = rs.getInt("kod");
                    String pName = rs.getString("produktnamn");
                    int quantity = rs.getInt("kvantitet");
                    int basePrice = rs.getInt("baspris");
                    int discounted_price = rs.getInt("rabattpris");
                    String supplName = rs.getString("produktleverantor");
                    int supplID = rs.getInt("leverantorid");
                    int discount_id = rs.getInt("rabattid");
                    int amount_sold = rs.getInt("totaltsolt");

                    System.out.println("Product code: " + Pcode + " | Product name: " + pName + " | Quantity: " + quantity + " | BasePrice: " + basePrice + " | Discounted_price: " + discounted_price
                            + " | Supplied by: " + supplName + " | Supplier ID: " + supplID + " | Discount ID: " + discount_id + " | Amount sold: " + amount_sold);
                }
            }
        }
    }

    public void discount_history() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");

            Statement statement = conn.createStatement();
            String view = "SELECT * FROM see_discount_history";
            ResultSet rs = statement.executeQuery(view);

            while (rs.next()) {
                int product_code = rs.getInt("product_code");
                String product_name = rs.getString("product_name");
                int discount_id = rs.getInt("discount_id");
                int basePrice = rs.getInt("basePrice");
                int discounted_price = rs.getInt("discounted_price");
                Date from_date = rs.getDate("from_date");
                Date to_date = rs.getDate("to_date");

                System.out.println(product_code + " | " + product_name + " | " + discount_id + " | " +
                        basePrice + " | " + discounted_price + " | " + from_date + " | " + to_date);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void maximumOrders() throws Exception {
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             CallableStatement function = con.prepareCall("{ call maximum_orders() }")) {  // No parameters, direct call

            // Execute the function and fetch the result set
            try (ResultSet rs = function.executeQuery()) {  // Use executeQuery to get the result set
                while (rs.next()) {
                    int month = rs.getInt("_month");  // Use correct column names
                    int productCode = rs.getInt("_product_code");
                    String productName = rs.getString("_product_name");
                    double totalSales = rs.getDouble("_total_sales");

                    System.out.println("Month: " + month + " | Product code: " + productCode + " | Product name: " + productName + " | Total sales: " + totalSales);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void maximumOrderss() throws Exception {
        String url = "jdbc:postgresql://pgserver.mau.se:5432/ao7408";
        String username = "ao7408";
        String password = "8ixdmx0u";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM maximum_orders()";
            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int _month = rs.getInt("_month");
                    int _productCode = rs.getInt("_product_code");
                    String _productName = rs.getString("_product_name");
                    double _totalSales = rs.getDouble("_total_sales");

                    System.out.println("Month: " + _month +
                            ", Product Code: " + _productCode +
                            ", Product Name: " + _productName +
                            ", Total Sales: " + _totalSales);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new Exception("Error executing the maximum_orders() function", e);
            }
        }
    }

    public void view_of_orders(int customer_id) throws Exception {
        String query = "SELECT * FROM view_of_orders(?)";
        try (Connection con = DriverManager.getConnection(
                "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, customer_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while ((rs.next())) {
                    int order_id = rs.getInt("order_idd");
                    int quantity_ordered = rs.getInt("quantity_orderedd");
                    int pCode = rs.getInt("product_codee");
                    String pName = rs.getString("product_namee");
                    Date date = rs.getDate("datee");

                    System.out.println("Order ID: " + order_id + " | Quantity ordered: " + quantity_ordered + " | Product code: " + pCode + " | Product name: " + pName + " | Date: " + date);
                }
            }
        }
    }

    public void created_new_order(int customer_id) {
        Scanner scanner = new Scanner(System.in);
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://pgserver.mau.se:5432/ao7408", "ao7408", "8ixdmx0u");
            String query = "{ ? = call created_new_order(?)}";
            CallableStatement callableStatement = con.prepareCall(query);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2, customer_id);
            callableStatement.execute();

            int orderID = callableStatement.getInt(1);
            System.out.println("Klart! Din nya order_id är: " + orderID);

            callableStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


