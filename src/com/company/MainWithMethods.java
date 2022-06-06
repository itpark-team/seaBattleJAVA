package com.company;

import java.util.Random;
import java.util.Scanner;

public class MainWithMethods {

    static class ShootCoordinates {
        public int iShoot;
        public int jShoot;

        public ShootCoordinates(int iShoot, int jShoot) {
            this.iShoot = iShoot;
            this.jShoot = jShoot;
        }
    }

    public enum FieldCell {
        ALIVE_SHIP('K'),
        DEAD_SHIP('X'),
        MISS('O'),
        EMPTY('.');

        private char value;

        FieldCell(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }

    public enum Player {
        USER("Игорок"),
        COMP("Компьютер"),
        NONE("Пока неизвестно");

        private String value;

        Player(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //region fields methods
    static int inputFieldSize() {
        int filedSize = 0;
        boolean inputResult;
        do {
            System.out.print("Пожалуйста введите размер поля для игры (от 3 до 10): ");
            inputResult = true;

            try {
                Scanner scanner = new Scanner(System.in);
                filedSize = scanner.nextInt();

                if (filedSize < 3 || filedSize > 10) {
                    throw new Exception();
                }
            } catch (Exception e) {
                inputResult = false;
                System.out.println("Ошибка. Введите число и в заданном диапазоне");
            }

        } while (inputResult == false);

        return filedSize;
    }

    static FieldCell[][] createField(int filedSize) {
        return new FieldCell[filedSize][filedSize];
    }

    static void clearField(FieldCell[][] field, int filedSize) {
        for (int i = 0; i < filedSize; i++) {
            for (int j = 0; j < filedSize; j++) {
                field[i][j] = FieldCell.EMPTY;
            }
        }
    }

    static void randomSetShips(FieldCell[][] field, int filedSize, int countShips) {
        Random random = new Random();

        for (int k = 0; k < countShips; k++) {
            int iShip, jShip;

            do {
                iShip = random.nextInt(filedSize);
                jShip = random.nextInt(filedSize);
            } while (field[iShip][jShip] != FieldCell.EMPTY);

            field[iShip][jShip] = FieldCell.ALIVE_SHIP;
        }
    }

    static Player getFirstStepPlayer() {
        Random random = new Random();

        if (random.nextInt(1000 + 1) > 500) {
            return Player.USER;
        } else {
            return Player.COMP;
        }
    }

    static void printClosedField(FieldCell[][] field, int filedSize) {
        for (int i = 0; i < filedSize; i++) {
            for (int j = 0; j < filedSize; j++) {
                if (field[i][j] == FieldCell.ALIVE_SHIP) {
                    System.out.print(FieldCell.EMPTY.getValue());
                } else {
                    System.out.print(field[i][j].getValue());
                }
            }
            System.out.println();
        }
    }

    static void printOpenedField(FieldCell[][] field, int filedSize) {
        for (int i = 0; i < filedSize; i++) {
            for (int j = 0; j < filedSize; j++) {
                System.out.print(field[i][j].getValue());
            }
            System.out.println();
        }
    }

    static ShootCoordinates compShoot(FieldCell[][] userField, int filedSize) {
        Random random = new Random();

        int iShoot, jShoot;

        do {
            iShoot = random.nextInt(filedSize);
            jShoot = random.nextInt(filedSize);
        } while (userField[iShoot][jShoot] == FieldCell.DEAD_SHIP || userField[iShoot][jShoot] == FieldCell.MISS);

        return new ShootCoordinates(iShoot, jShoot);
    }

    static int inputNumber(String message, int min, int max) {
        boolean inputResult = true;
        int number = 0;

        do {
            try {
                Scanner scanner = new Scanner(System.in);
                inputResult = true;
                System.out.print(message);
                number = scanner.nextInt();

                if (number < min || number > max) {
                    throw new Exception();
                }

            } catch (Exception e) {
                inputResult = false;
                System.out.println("Ошибка. Проверьте правильность ввода данных.");
            }

        } while (!inputResult);

        return number;
    }

    static ShootCoordinates userShoot(FieldCell[][] compField, int filedSize) {
        int iShoot, jShoot;
        boolean shootResult;

        do {
            iShoot = inputNumber(String.format("введите строчку для выстрела от %d до %d: ", 1, filedSize), 1, filedSize) - 1;

            jShoot = inputNumber(String.format("введите столбец для выстрела от %d до %d: ", 1, filedSize), 1, filedSize) - 1;

            shootResult = true;
            if (compField[iShoot][jShoot] == FieldCell.DEAD_SHIP || compField[iShoot][jShoot] == FieldCell.MISS) {
                shootResult = false;
                System.out.println("Ошибка выстрела. Вы в эту ячейку вы уже стреляли");
            }

        } while (!shootResult);

        return new ShootCoordinates(iShoot, jShoot);
    }

    static Player changePlayer(Player currentPlayer) {
        if (currentPlayer == Player.USER) {
            return Player.COMP;
        } else if (currentPlayer == Player.COMP) {
            return Player.USER;
        }
        return Player.NONE;
    }

    static Player checkHitToEmptyCell(FieldCell[][] field, ShootCoordinates shootCoordinates, Player currentPlayer) {
        if (field[shootCoordinates.iShoot][shootCoordinates.jShoot] == FieldCell.EMPTY) {
            field[shootCoordinates.iShoot][shootCoordinates.jShoot] = FieldCell.MISS;
            return changePlayer(currentPlayer);
        }

        return currentPlayer;
    }

    static int checkHitToAliveShip(FieldCell[][] field, ShootCoordinates shootCoordinates, int countUserShips) {
        if (field[shootCoordinates.iShoot][shootCoordinates.jShoot] == FieldCell.ALIVE_SHIP) {
            field[shootCoordinates.iShoot][shootCoordinates.jShoot] = FieldCell.DEAD_SHIP;
            countUserShips--;
        }

        return countUserShips;
    }

    static Player getWinner(int countCompShips, int countUserShips) {
        if (countCompShips == 0) {
            return Player.USER;
        } else if (countUserShips == 0) {
            return Player.COMP;
        }
        return Player.NONE;
    }

    //endregion

    //region utils method
    static void waitPressEnter() {
        System.out.println("Для продолжения нажмите <Enter>");
        new Scanner(System.in).nextLine();
    }

    static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    static void printlnMessage(String message) {
        System.out.println(message);
    }
    //endregion

    public static void main(String[] args) {
        FieldCell[][] compField, userField;
        int filedSize = 0;
        int countCompShips, countUserShips;

        Player currentPlayer, winPlayer = Player.NONE;

        printlnMessage("Добро пожаловать в моской бой против компьютера");

        filedSize = inputFieldSize();
        countCompShips = countUserShips = filedSize;

        compField = createField(filedSize);
        userField = createField(filedSize);

        clearField(compField, filedSize);
        clearField(userField, filedSize);

        randomSetShips(compField, filedSize, countCompShips);
        randomSetShips(userField, filedSize, countUserShips);

        currentPlayer = getFirstStepPlayer();
        printlnMessage("первым ходит " + currentPlayer.getValue());

        waitPressEnter();

        while (winPlayer == Player.NONE) {
            clearScreen();

            printlnMessage("поле компьютера");
            printClosedField(compField, filedSize);

            printlnMessage("поле игрока");
            printOpenedField(userField, filedSize);

            if (currentPlayer == Player.COMP) {
                printlnMessage("Выстрел компьютера.");
                waitPressEnter();

                ShootCoordinates shootCoordinates = compShoot(userField, filedSize);

                currentPlayer = checkHitToEmptyCell(userField, shootCoordinates, currentPlayer);

                countUserShips = checkHitToAliveShip(userField, shootCoordinates, countUserShips);

            } else if (currentPlayer == Player.USER) {
                printlnMessage("Выстрел игрока.");

                ShootCoordinates shootCoordinates = userShoot(compField, filedSize);

                currentPlayer = checkHitToEmptyCell(compField, shootCoordinates, currentPlayer);

                countCompShips = checkHitToAliveShip(compField, shootCoordinates, countCompShips);
            }

            winPlayer = getWinner(countCompShips, countUserShips);
        }


        clearScreen();

        printlnMessage("поле компьютера");
        printOpenedField(compField, filedSize);

        printlnMessage("поле игрока");
        printOpenedField(userField, filedSize);

        printlnMessage("Победил " + winPlayer.getValue());
    }
}
