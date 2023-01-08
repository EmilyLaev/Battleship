package com.demo.battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Battleship {

    //main method
    //This is to create each type of ship as a different object of the class ship
    //A field is also created for the first player called "myField"
    public static void main(String[] args) {
        System.out.println("My Battleship");
        Field myField = new Field(10, "myField");
        Ship aC = new Ship("Aircraft Carrier", 5);
        Ship bS = new Ship("Battleship", 4);
        Ship sB = new Ship("Submarine", 3);
        Ship cR = new Ship("Cruiser", 3);
        Ship dS = new Ship("Destroyer", 2);
        createField(myField);

        //These are default values and will be changed once the player sets the ship on the field
        String cord1 = "00";
        String cord2 = "00";

        //ask for and place each ship on field
        placeShip(myField, aC, cord1, cord2);
        placeShip(myField, bS, cord1, cord2);
        placeShip(myField, sB, cord1, cord2);
        placeShip(myField, cR, cord1, cord2);
        placeShip(myField, dS, cord1, cord2);

        //Second player repeats actions with a second created field
        changePlayer();
        System.out.println("Player 2, set your ships!");

        
        Field theirField = new Field(10, "Their Field");
        Ship aC2 = new Ship("Aircraft Carrier 2", 5);
        Ship bS2 = new Ship("Battleship 2", 4);
        Ship sB2 = new Ship("Submarine 2", 3);
        Ship cR2 = new Ship("Cruiser 2", 3);
        Ship dS2 = new Ship("Destroyer 2", 2);
        createField(theirField);

        //Placing the five ships on the second player's field
        placeShip(theirField, aC2, cord1, cord2);
        placeShip(theirField, bS2, cord1, cord2);
        placeShip(theirField, sB2, cord1, cord2);
        placeShip(theirField, cR2, cord1, cord2);
        placeShip(theirField, dS2, cord1, cord2);

        //Switch back to first player
        changePlayer();

        System.out.println("The game starts!");

        //playGame
        //This is the main while loop that runs the game play until win conditions are met
        //Players are able to fire onto other's field and program determines if a ship was hit, if ship has sunk,
        //Loop ends when one player's ships have all sunk
        boolean playOn = true;
        while (playOn) {
            createFog(theirField);
            System.out.println("---------------------");
            createField(myField);
            System.out.println("Player 1, it's your turn:");
            fireShot(theirField);
            dSS(theirField, aC2);
            dSS(theirField, bS2);
            dSS(theirField, sB2);
            dSS(theirField, cR2);
            dSS(theirField, dS2);
            if (aC2.sank && bS2.sank && sB2.sank && cR2.sank && dS2.sank) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                playOn = false;
                continue;
            }
            changePlayer();

            createFog(myField);
            System.out.println("---------------------");
            createField(theirField);
            System.out.println("Player 2, it's your turn:");
            fireShot(myField);
            dSS(myField, aC);
            dSS(myField, bS);
            dSS(myField, sB);
            dSS(myField, cR);
            dSS(myField, dS);
            if (aC.sank && bS.sank && sB.sank && cR.sank && dS.sank) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                playOn = false;
            }
            changePlayer();
        }
    }

    //This method prints out a field with all the ships shown
    public static void createField(Field mine) {
        int a = 64;
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            a++;
            char b = (char) a;
            System.out.print(b);
            for (int j = 0; j < 10; j++) {
                if (mine.field[i][j] <= 4) { // || mine.field[i][j] == 1 || mine.field[i][j] == 2 || mine.field[i][j] == 3) {
                    System.out.print(" ~");
                } else if (mine.field[i][j] == 5) {
                    System.out.print(" O");
                } else if (mine.field[i][j] == 11) {
                    System.out.print(" X");
                 } else if (mine.field[i][j] > 5 || mine.field[i][j] < 11) {
                    System.out.print(" M");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //This method prints out field with with only the hit ships displayed, (the non-hits are hidden)
    public static void createFog(Field mine) {
        int a = 64;
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            a++;
            char b = (char) a;
            System.out.print(b);
            for (int j = 0; j < 10; j++) {
               if (mine.field[i][j] == 11) {
                    System.out.print(" X");
                } else if (mine.field[i][j] > 5 && mine.field[i][j] < 11) {
                    System.out.print(" M");
                } else {
                    System.out.print(" ~");
                }
            }
            System.out.println();
        }
        //System.out.println();
    }

    //method placeShip
    //This method calls the askShip method which asks for coordinates, then it creates a temporary field
    //The temporaray field checks the values to ensure the spot is clear, if not user is ask for new coordinates
    public static void placeShip(Field field1, Ship ship, String cord1, String cord2) {
        boolean canSet = false; 
        while (!canSet) {
            askShip(ship, cord1, cord2);
            Field temp = new Field(10, "TEMP");
            temp.setField(copyArray(field1.getField()));
            addShip(temp, ship);
            addRShip(temp, ship);
            canSet = checkField(temp, 5);
            if (!canSet) {
                System.out.println("Error! Cannot set boat here!");
            }
        }
        addShip(field1, ship);
        addRShip(field1, ship);
        //for (int i = 0; i < ship.size; i++) {
        //    System.out.println(Arrays.toString(ship.allCords[i]));
        //}
        createField(field1);
    }

    //This method asks for the coordinates of a ship (note defalut values are used for the cord1 and cord2)
    //These coordinates are checked to ensure they fit field and don't overlap another ship, and turned into integer values 
    //The ships object is updated to have the new coordinates and direction
    public static void askShip(Ship ship, String cord1, String cord2) {
        String str = String.format("Enter the coordinates of the %s (%d cells)", ship.getName(), ship.getSize());
        System.out.println(str);
        boolean posCor = false;
        Scanner scan = new Scanner(System.in);
        while (!posCor) {
            boolean correctCord1 = false;
            boolean correctCord2 = false;
            while (!correctCord1 || !correctCord2) {
                cord1 = scan.next();
                cord2 = scan.next();
                correctCord1 = checkString(cord1);
                correctCord2 = checkString(cord2);
                if (!correctCord1 || !correctCord2) {
                    System.out.println("Error! Invalid Coordinates, please try again");
                }
            }
            int[] cor1 = setCord(cord1);
            int[] cor2 = setCord(cord2);
            int dir = checkDir(cor1, cor2);
            ship.setStrCord(cor1);
            ship.setDir(dir);
            posCor = checkCor(ship, cor2);
            //System.out.println(dir);
            if (!posCor) {
                System.out.println("Error! Wrong ship size! Try Again");
                //System.out.println("This are ship cords " + Arrays.toString(ship.allCords));
            }
        }
        ship.rotateShip();
        ship.setAllCords();
        //System.out.println("Works For Now" + ship.dir);
    }

    //This is a method that asks the user to fire a shot then checks if spot is available
    //If not the user is asked to try again, else the coordinate is set as an integer
    public static int[] askShot(String cord) {
        System.out.println("Take a shot!");
        Scanner scan = new Scanner(System.in);
        boolean CorCord = false;
        while (!CorCord) {
            cord = scan.next();
            CorCord = checkString(cord);
            if (!CorCord) {
                System.out.println("Error! Invalid Coordinate, try again");
            }
        }
        return setCord(cord);
    }

    //This is a method to check if the user entered proper coordinates by checking the value is within field size
    public static boolean checkString(String scan) {

        if (scan.length() == 2) {
            int a = scan.charAt(0) - 65;
            int b = scan.charAt(1) - 49;
            if (a >= 0 && a < 11 && b >= 0 && b < 10) {
                return true;
            } else {
                return false;
            }
        } else if (scan.length() == 3) {
            int a = scan.charAt(0) - 65;
            if (a >= 0 && a < 11 && scan.charAt(1) == '1' && scan.charAt(2) == '0') {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //This method turns user input from string into integer form
    public static int[] setCord(String scan) {
        int let;
        int num;
        if (scan.length() == 2) {
            let = scan.charAt(0) - 65;
            num = scan.charAt(1) - 49;
        } else {
            let = scan.charAt(0) - 65;
            num = 9;
        }
        //System.out.println(let + " " + num);
        return new int[]{let, num};
    }

    //This method checks that the second coordinate is possible based on ship size
    public static boolean checkCor(Ship ship, int[] cor2) {
        if (ship.dir != 0) {
            if (ship.dir == 1) {
                return ship.size == cor2[1] - ship.strCord[1] + 1;
            } else if (ship.dir == 2) {
                return ship.size == ship.strCord[1] - cor2[1] + 1;
            } else if (ship.dir == 3) {
                return ship.size == cor2[0] - ship.strCord[0] + 1;
            } else if (ship.dir == 4) {
                return ship.size == ship.strCord[0] - cor2[0] + 1;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //A method to find which direction the ship is turned
    public static int checkDir(int[] corD1, int[] corD2) {
        if (corD1[0] == corD2[0]) {
            if (corD1[1] < corD2[1]) {
                return 1;
            } else {
                return 2;
            }
        } else if (corD1[1] == corD2[1]) {
            if (corD1[0] < corD2[0]) {
                return 3;
            } else {
                return 4;
            }
        } else {
            return 0;
        }
    }

    //A method to compare values to see if ship sank (Did Ship Sink)
    public static void dSS(Field field, Ship ship) {
        if (!ship.sank) {
            int hp = 0;
            for (int i = 0; i < ship.size; i++) {
                int val = field.getSpot(ship.allCords[i]);
                //System.out.println("Need to check " + Arrays.toString(ship.allCords[i]) + " " + val);
                if (val == 11) {
                    hp++;
                }
            }
            if (hp == ship.size) {
                String str = String.format("You sank a ship! %s", ship.getName());
                System.out.println(str);
                ship.setSank(true);
            }
        }
    }

    //A simple method to change the value of a spot on the field
    public static void markSpot(Field field, int[] cord, int value) {
        field.field[cord[0]][cord[1]] = field.field[cord[0]][cord[1]] + value;
    }

    //A method to retrieve value of field at specific spot
    //Not used in program, can just access object getter
    public static boolean checkSpot(Field field, int[] cor, int value) {
        return field.getSpot(cor) == value;
    }

    //This method checks that all values within the field are possible
    public static boolean checkField(Field field, int max) {
        int works = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (field.field[i][j] > max) {
                    works++;
                }
            }
        } return works == 0; }

    //Method changePlayer
    //This method simply prints out a line to inform the user to swtich players
    //The program waits until the enter key is pressed before moving on
    public static void changePlayer() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Press Enter and pass the move to another player");
        while (true) {
            String ent = scan.nextLine();
            if (ent.equals("")) {
                break;
            }
        }
    }

    //This method sets the ship on the field by changing field values
    public static Field addShip(Field field, Ship ship) {
        int[] corD = new int[2];
        for (int i = 0; i < 2; i++) {
            corD[i] = ship.strCord[i];
        }
        if (ship.dir == 1) {
            for (int i = 0; i < ship.size; i++) {
                markSpot(field, corD, 4);
                corD[1]++;
            }
        } else if (ship.dir == 3) {
            for (int i = 0; i < ship.size; i++) {
                markSpot(field, corD, 4);
                corD[0]++;
            }
        }
        return field;
    }

    //This method sets the ship on the field by changing field values
    public static Field addRShip(Field field, Ship ship) {
        int[] cord = new int[2];
        for (int i = 0; i < 2; i++) {
            cord[i] = ship.strCord[i];
        }
        if (ship.dir == 1) {
            cord[0]--;
            cord[0]--;
            cord[1]--;
            for (int i = 0; i < ship.size + 2; i++) {
                for (int j = 0; j < 3; j++) {
                    try {
                        cord[0]++;
                        markSpot(field, cord, 1);
                    } catch (Exception e) {
                        continue;
                    }
                }
                cord[0]--;
                cord[0]--;
                cord[0]--;
                cord[1]++;
            }
        } else if (ship.dir == 3) {
            cord[0]--;
            cord[1]--;
            cord[1]--;
            for (int i = 0; i < ship.size + 2; i++) {
                for (int j = 0; j < 3; j++) {
                    try {
                        cord[1]++;
                        markSpot(field, cord, 1);
                    } catch (Exception e) {
                        continue;
                    }
                }
                cord[0]++;
                cord[1]--;
                cord[1]--;
                cord[1]--;
            }
        }
        return field;
    }

    //This is a method to copy an integer array
    public static int[][] copyArray(int[][] a) {
        int[][] b = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                b[i][j] = a[i][j];
            }
        }
        return b;
    }

    //This method compares field values and tells user if they hit or missed a ship
    //this method was not used in final program
    public static void hitMiss(Field field, int[] cor) {
        int val = field.getSpot(cor);
        if (val == 10) {
            System.out.println("You hit a ship!");
        } else if (val < 10) {
            System.out.println("You missed!");
        }
    }

    //This method asks users to fire a shot, checks coordinates, marks the field
    //then compares field values and tells user if they hit or missed a ship
    public static void fireShot(Field field) {
        String cord = "00";
        int[] cor = {0, 0};
        boolean okShot = false;
        cor = askShot(cord);
        int value = field.getSpot(cor);
        if (value == 5) {
            System.out.println("You hit a ship!");
            markSpot(field, cor, 6);
        } else if (value == 11) {
            System.out.println("You hit a ship!");
        } else if (value < 5) {
            System.out.println("You missed!");
            markSpot(field, cor, 6);
        } else if (value > 5 && value < 11) {
            System.out.println("You missed!");
        }
    }

    //This is the ship class.  Each ship object had a direction, name, size, an integer array for coordinates
    //and a boolean stating if the ship has sank
    public static class Ship {
        private final String name;
        private final int size;
        private int dir;
        private int[] strCord;
        private int[][] allCords;
        private boolean sank;

        public Ship(String name, int size) {
            this.name = name;
            this.size = size;
            this.dir = 0;
            this.strCord = new int[2];
            this.sank = false;
            this.allCords = new int[size][2];
        }

        public int getSize() { return size; }

        public void setDir(int dir) { this.dir = dir; }

        public void setStrCord(int[] strCord) { this.strCord = strCord; }

        public String getName() { return name; }

        public void setSank(boolean sank) { this.sank = sank; }

        public void rotateShip() {
            if (dir == 2) {
                strCord[1] = strCord[1] - (size - 1);
                dir = 1;
            } else if (dir == 4) {
                strCord[0] = strCord[0] - (size - 1);
                dir = 3;
            }
        }

        //This method resets the coordinates after the direction and first coordinate are set
        public void setAllCords() {
            int i = 1;
            this.allCords[0] = strCord;
            if (dir == 1) {
                for (; i < size; i++) {
                    this.allCords[i][1] = allCords[i - 1][1] + 1;
                    this.allCords[i][0] = allCords[0][0];
                }
            } else if (dir == 3) {
                for (; i < size; i++) {
                    this.allCords[i][0] = allCords[i - 1][0] + 1;
                    this.allCords[i][1] = allCords[0][1];
                }
            }
        }

        public int[][] getAllCords() { return allCords; }
    }

    //This is the class Field, it takes an integer array, a size and name
    //The values of the array determine where the ships are placed, what is visable
    //and if a ship is hit or not.  This is the main information storage of the game
    public static class Field {
        private int[][] field;
        private int size;
        private String name;

        public Field(int size, String name) {
            this.size = size;
            this.name = name;
            int[][] bulk = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    bulk[i][j] = 0;
                }
            }
            this.field = bulk;
        }

        public void setField(int[][] field) { this.field = field; }

        public int[][] getField() { return field; }

        public int getSpot(int[] cor) {
            return field[cor[0]][cor[1]];
        }
    }
}
