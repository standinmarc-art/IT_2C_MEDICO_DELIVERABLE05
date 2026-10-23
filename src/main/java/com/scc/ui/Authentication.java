package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;
import java.util.Scanner;

public class Authentication {
    private final Scanner sc = new Scanner(System.in);
    private final UserDao dao = new UserDao();

    public void start() {
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Username: ");
        String uname = sc.nextLine().trim();
        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();

        User user = dao.getUserByUsername(uname);

        if (user == null) {
            System.out.println("[✗] Username not found.");
            return;
        }
        if (!user.password().equals(pwd)) {
            System.out.println("[✗] Incorrect password.");
            return;
        }
        if (!"APPROVED".equals(user.accountStatus())) {
            System.out.println("[!] Account status: " + user.accountStatus() +
                    " — Approval required before login.");
            return;
        }

        System.out.println("[✓] Login successful!");

        switch (user.role()) {
            case "ADMIN" -> new AdminMenu(user).show();
            case "SECURITY" -> new SecurityMenu(user).show();
            case "DELIVERY" -> new DeliveryMenu(user).show();
            default -> System.out.println("[✗] Unknown role.");
        }
    }
}