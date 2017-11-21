package ru.ex4.apibt;


import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.Currency;
import ru.ex4.apibt.logic.OpenOrder;
import ru.ex4.apibt.logic.Wait;
import ru.ex4.apibt.service.InitBaseService;
import ru.ex4.apibt.thread.AutoAuction;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Logs.info("  ---------- hello  ---------- ");

        InitBaseService.init();

        AutoAuction autoAuction = new AutoAuction();

        Scanner scanner = new Scanner(System.in);
        showMenu();

        while (true) {
            String comm = scanner.nextLine();

            if (comm.equals("1")) {
                autoAuction.start();
            } else if (comm.equals("2")) {
                System.out.println(" -2- ");
            } else if (comm.equals("3")) {
                System.out.println(" -3- ");
            } else if (comm.equals("0") || comm.equals("quit")) {
                autoAuction.interrupt();
                break;
            } else {
                showMenu();
            }


        }

        Logs.info("  ---------- end ------------- ");
    }

    private static void showMenu() {
        System.out.println("Введите: ");
    }


}
