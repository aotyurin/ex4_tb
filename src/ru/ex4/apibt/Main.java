package ru.ex4.apibt;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.menuAuction.*;
import ru.ex4.apibt.service.InitBaseService;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application{
    public static void main(String[] args) throws IOException {
        Logs.info("  ---------- hello  ---------- ");

        InitBaseService.init();

//        ExFactory exFactory = ExFactory.exFactoryInstance();
//        List<OrderBook> orderBook = exFactory.getOrderBook(IExConst.PAIR, null);


        AutoAuctionThread autoAuction = new AutoAuctionThread();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            String comm = scanner.nextLine();

            if (comm.equals("1a") || comm.equals("auto")) {
                autoAuction.start();
            } else if (comm.equals("2a") || comm.equals("buy")) {
                System.out.println(" -2a- ");
                autoAuction.interrupt();
                new BuyAuction().start();
            } else if (comm.equals("3a") || comm.equals("sell")) {
                System.out.println(" -3a- ");
                autoAuction.interrupt();
                new SellAuction().start();
            } else if (comm.equals("4a") || comm.equals("order")) {
                System.out.println(" -4a- ");
                autoAuction.interrupt();
                new OrderCancel().start();
            } else if (comm.equals("5a") || comm.equals("calc")) {
                System.out.println(" -5a- ");
                new CalcAuction().start();
            } else if (comm.equals("6a") || comm.equals("info")) {
                System.out.println(" -6a- ");
                new InfoAuction().start();
            } else if (comm.equals("7a") || comm.equals("ui")) {
                System.out.println(" -7a- ");
                launch(args);
            } else if (comm.equals("0a") || comm.equals("quit")) {
                autoAuction.interrupt();
                break;
            }
        }

        Logs.info("  ---------- end ------------- ");
    }

    private static void showMenu() {
        System.out.println("Введите команду меню:\n" +
                " =1= автоторги \t\t[1a/auto]\n" +
                " =2= купить\t\t\t[2a/buy]\n" +
                " =3= продать \t\t[3a/sell]\n" +
                " =4= отменить ордер\t[4a/order]\n" +
                " =5= калькулятор \t[5a/calc]\n" +
                " =6= инфо \t\t[6a/info]\n" +
                " =7= GUI \t\t[7a/ui]\n" +
                " =0= выход \t\t\t[0a/quit] \n ");
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        initLayout(primaryStage);
    }

    private void initLayout(Stage primaryStage) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/BaseView.fxml"));
        primaryStage.setTitle(" - - - - -");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
