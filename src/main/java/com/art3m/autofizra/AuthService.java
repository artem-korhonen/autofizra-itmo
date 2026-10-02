package com.art3m.autofizra;

import java.util.Scanner;

import api.myitmo.MyItmo;

public class AuthService {
    public static MyItmo auth(Scanner scanner) {
        MyItmo myItmo = new MyItmo();
        System.out.print("Введите isu: ");
        String isu = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.println();

        myItmo.auth(isu, password);

        return myItmo;
    }

    public static MyItmo auth(String isu, String password) throws RuntimeException {
        MyItmo myItmo = new MyItmo();
        myItmo.auth(isu, password);

        return myItmo;
    }
}
