
import java.util.*;

class Product {
    String id, name;
    double price;
    int stock;

    Product(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getSubtotal() {
        return product.price * quantity;
    }
}

class ShoppingCart {
    HashMap<String, Product> products = new HashMap<>();
    LinkedList<CartItem> cart = new LinkedList<>();
    Stack<LinkedList<CartItem>> undoStack = new Stack<>();

    ShoppingCart() {
        products.put("P101", new Product("P101", "Laptop", 55000, 10));
        products.put("P102", new Product("P102", "Mouse", 800, 25));
        products.put("P103", new Product("P103", "Keyboard", 2500, 15));
        products.put("P104", new Product("P104", "USB Hub", 1200, 20));
        products.put("P105", new Product("P105", "Smartwatch", 4500, 5));
    }

    void saveState() {
        LinkedList<CartItem> copy = new LinkedList<>();

        for (CartItem item : cart) {
            copy.add(new CartItem(item.product, item.quantity));
        }

        undoStack.push(copy);
    }

    void showProducts() {
        System.out.println("\n--- PRODUCT CATALOG ---");

        for (Product p : products.values()) {
            System.out.println(p.id + " | " + p.name +
                    " | Rs." + p.price + " | Stock: " + p.stock);
        }
    }

    void addToCart(String id, int qty) {
        Product p = products.get(id);

        if (p == null || qty <= 0) {
            System.out.println("Invalid product ID or quantity!");
            return;
        }

        CartItem existing = null;

        for (CartItem item : cart) {
            if (item.product.id.equals(id)) {
                existing = item;
                break;
            }
        }

        int currentQty = (existing == null) ? 0 : existing.quantity;

        if (currentQty + qty > p.stock) {
            System.out.println("Insufficient stock!");
            return;
        }

        saveState();

        if (existing != null) {
            existing.quantity += qty;
        } else {
            cart.add(new CartItem(p, qty));
        }

        System.out.println("Product added successfully!");
    }

    void updateQuantity(String id, int qty) {
        for (CartItem item : cart) {
            if (item.product.id.equals(id)) {
                if (qty <= 0 || qty > item.product.stock) {
                    System.out.println("Invalid quantity!");
                    return;
                }

                saveState();
                item.quantity = qty;
                System.out.println("Quantity updated!");
                return;
            }
        }

        System.out.println("Product not found in cart!");
    }

    void removeFromCart(String id) {
        for (CartItem item : cart) {
            if (item.product.id.equals(id)) {
                saveState();
                cart.remove(item);
                System.out.println("Product removed!");
                return;
            }
        }

        System.out.println("Product not found!");
    }

    void undo() {
        if (undoStack.empty()) {
            System.out.println("No action to undo!");
            return;
        }

        cart = undoStack.pop();
        System.out.println("Last action undone!");
    }

    void displayCart() {
        double subtotal = 0;

        System.out.println("\n--- SHOPPING CART ---");

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        for (CartItem item : cart) {
            double amount = item.getSubtotal();
            subtotal += amount;

            System.out.println(item.product.name +
                    " | Quantity: " + item.quantity +
                    " | Subtotal: Rs." + amount);
        }

        double tax = subtotal * 0.18;
        double total = subtotal + tax;

        System.out.println("-------------------------");
        System.out.println("Subtotal: Rs." + subtotal);
        System.out.println("GST (18%): Rs." + tax);
        System.out.println("Grand Total: Rs." + total);
    }
}

public class onlineshoppingcart {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart shop = new ShoppingCart();

        while (true) {
            System.out.println("\n===== ONLINE SHOPPING CART =====");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. Update Quantity");
            System.out.println("4. Remove Product");
            System.out.println("5. View Cart");
            System.out.println("6. Undo Last Action");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    shop.showProducts();
                    break;

                case 2:
                    System.out.print("Enter Product ID: ");
                    String id = sc.next();
                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    shop.addToCart(id, qty);
                    break;

                case 3:
                    System.out.print("Enter Product ID: ");
                    id = sc.next();
                    System.out.print("Enter New Quantity: ");
                    qty = sc.nextInt();
                    shop.updateQuantity(id, qty);
                    break;

                case 4:
                    System.out.print("Enter Product ID: ");
                    id = sc.next();
                    shop.removeFromCart(id);
                    break;

                case 5:
                    shop.displayCart();
                    break;

                case 6:
                    shop.undo();
                    break;

                case 7:
                    System.out.println("Thank you for shopping!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
