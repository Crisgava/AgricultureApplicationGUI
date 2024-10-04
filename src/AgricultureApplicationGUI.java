import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Product class for handling product details
class Product {
    private final int productId;
    private final String name;
    private final String category;
    private final double price;
    private int quantity;

    public Product(int productId, String name, String category, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Category: %s, Price: %.2f, Quantity: %d",
                productId, name, category, price, quantity);
    }
}

// User class for managing user roles and details
class User {
    private final int userId;
    private final String username;
    private String password;
    private final String role; // Admin, Seller, Buyer

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return String.format("User ID: %d, Username: %s, Role: %s", userId, username, role);
    }
}

// Order class for handling order details
class Order {
    private final int orderId;
    private final User user;
    private final Product product;
    private final int quantity;
    private String status; // Pending, Shipped, Delivered

    public Order(int orderId, User user, Product product, int quantity, String status) {
        this.orderId = orderId;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public User getUser() { return user; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Order ID: %d, User: %s, Product: %s, Quantity: %d, Status: %s",
                orderId, user.getUsername(), product.getName(), quantity, status);
    }
}

// GUI Application for the Agriculture Management System
public class AgricultureApplicationGUI extends JFrame {

    private static List<Product> productCatalog = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static int productCounter = 1;
    private static int userCounter = 1;
    private static int orderCounter = 1;

    private JTextArea displayArea;
    private JTextField usernameField;
    private JComboBox<String> productDropdown;
    private JTextField quantityField;

    public AgricultureApplicationGUI() {
        // Set up the GUI
        setTitle("Agriculture Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        usernameField = new JTextField(10);
        productDropdown = new JComboBox<>();
        quantityField = new JTextField(5);

        JButton addProductButton = new JButton("Add Product");
        JButton viewProductsButton = new JButton("View Products");
        JButton placeOrderButton = new JButton("Place Order");
        JButton viewOrdersButton = new JButton("View Orders");

        // Set up the panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Product:"));
        inputPanel.add(productDropdown);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(addProductButton);
        inputPanel.add(viewProductsButton);
        inputPanel.add(placeOrderButton);
        inputPanel.add(viewOrdersButton);

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Populate sample data
        populateSampleData();

        // Add action listeners
        addProductButton.addActionListener(e -> addProduct());
        viewProductsButton.addActionListener(e -> displayProducts());
        placeOrderButton.addActionListener(e -> placeOrder());
        viewOrdersButton.addActionListener(e -> displayOrders());
    }

    // Method to populate sample users and products
    private void populateSampleData() {
        // Add sample users
        users.add(new User(userCounter++, "admin", "password", "Admin"));
        users.add(new User(userCounter++, "seller", "password", "Seller"));
        users.add(new User(userCounter++, "buyer", "password", "Buyer"));

        // Add sample products
        productCatalog.add(new Product(productCounter++, "Wheat", "Grain", 50.0, 100));
        productCatalog.add(new Product(productCounter++, "Corn", "Grain", 30.0, 150));
        productCatalog.add(new Product(productCounter++, "Tomato", "Vegetable", 10.0, 200));

        // Populate product dropdown
        for (Product product : productCatalog) {
            productDropdown.addItem(product.getName());
        }
    }

    // Method to add a new product
    private void addProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter Product Name:");
        String category = JOptionPane.showInputDialog(this, "Enter Product Category:");
        double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Product Price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Product Quantity:"));

        Product product = new Product(productCounter++, name, category, price, quantity);
        productCatalog.add(product);
        productDropdown.addItem(name);

        displayArea.append("Product added: " + product + "\n");
    }

    // Method to display all products
    private void displayProducts() {
        displayArea.setText("--- Product Catalog ---\n");
        for (Product product : productCatalog) {
            displayArea.append(product + "\n");
        }
    }

    // Method to place an order
    private void placeOrder() {
        String username = usernameField.getText();
        User user = findUserByUsername(username);

        if (user == null) {
            displayArea.setText("User not found. Please enter a valid username.\n");
            return;
        }

        String productName = (String) productDropdown.getSelectedItem();
        int quantity = Integer.parseInt(quantityField.getText());
        Product product = findProductByName(productName);

        if (product == null || product.getQuantity() < quantity) {
            displayArea.setText("Product not available or insufficient quantity.\n");
            return;
        }

        // Update product quantity
        product.setQuantity(product.getQuantity() - quantity);

        Order order = new Order(orderCounter++, user, product, quantity, "Pending");
        orders.add(order);

        displayArea.append("Order placed: " + order + "\n");
    }

    // Method to display all orders
    private void displayOrders() {
        displayArea.setText("--- Order List ---\n");
        for (Order order : orders) {
            displayArea.append(order + "\n");
        }
    }

    // Helper method to find a user by username
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    // Helper method to find a product by name
    private Product findProductByName(String name) {
        for (Product product : productCatalog) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgricultureApplicationGUI app = new AgricultureApplicationGUI();
            app.setVisible(true);
        });
    }
}









	

