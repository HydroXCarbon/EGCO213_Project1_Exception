//6513135 Purin Pongpanich
//6513161 Jarupat Chodsitanan
//6513163 Chalisa Buathong
package Project1_135;

import java.util.Collections;
import java.util.Scanner;
import java.io.*;
import Project1_135.module.Room;
import Project1_135.module.Meal;
import Project1_135.module.Booking;

public final class Main {

    public static void main(String[] args) {
        //Create instance of Main
        Main mainInstance = new Main();

        //Input file path
        String inputPath = "src/main/java/Project1_135/";
        String hotelPath = inputPath + "hotel.txt";
        String bookingPath = inputPath + "bookings.txt";

        boolean hotelFound = false;
        boolean bookingFound = false;
        Room room = new Room();
        Meal meal = new Meal();
        Booking booking = new Booking();

        //Insert data from file hotel to Class Room and Meal
        while(!hotelFound) {
            try {
                Scanner scan = new Scanner(new File(hotelPath));
                mainInstance.addHotel(scan, hotelPath, room, meal);
                hotelFound=true;
            } catch (Exception e) {
                System.out.println(e);
                System.out.print("Enter file name for hotel data = ");
                Scanner userInput = new Scanner(System.in);
                String newFileName = userInput.nextLine();
                hotelPath = inputPath + newFileName;
            }
        }

        //Insert data from file booking to Class Booking and Customer
        while(!bookingFound) {
            try {
                Scanner scan = new Scanner(new File(bookingPath));
                mainInstance.addBooking(scan, bookingPath, booking);
                bookingFound=true;
            } catch (Exception e) {
                System.out.println(e);
                System.out.print("Enter file name for booking data = ");
                Scanner userInput = new Scanner(System.in);
                String newFileName = userInput.nextLine();
                bookingPath = inputPath + newFileName;
            }
        }

        // Booking processing
        for(int bookingID : booking.getBookingIDs()){
            String customerName = booking.getCustomerName(bookingID);
            booking.fetchCustomerCashBack(bookingID);
            int cashBack = booking.getCashBack(bookingID);
            int night = booking.getNight(bookingID);
            int[] roomTypes = booking.getRoomTypes(bookingID);
            double[] roomPrice = room.getRoomPrice();
            int redeem = 0;
            double totalRoomPrice = 0;
            double totalMealPrice = 0;
            for (int i = 0; i < roomTypes.length; i++) {
                try {
                    if (i == roomTypes.length - 1) {
                        totalMealPrice += night * meal.getPrice() * roomTypes[i];
                        meal.addTotalSaleInUnit(night * roomTypes[i]);
                        meal.addTotalSaleInCash(totalMealPrice);
                    } else {
                        totalRoomPrice += night * roomPrice[i] * roomTypes[i];
                        room.addTotalSaleInUnit(room.getRoomType(i), night *roomTypes[i]);
                        room.addTotalSaleInCash(room.getRoomType(i), roomTypes[i] * roomPrice[i] * night );
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            double totalBill = totalRoomPrice + totalMealPrice;
            if(cashBack > totalBill/2){
                redeem = (int)totalBill/2;
            }else{
                redeem = cashBack;
            }
            int newCashback = (int) (totalRoomPrice * 0.05);
            double finalBill = totalBill - redeem;
            booking.setCashBack(bookingID, newCashback);

            // Print booking summary
            System.out.printf("Booking %3d, %7s, %2d nights   >> ", bookingID, customerName, night);
            for(int i = 0; i < roomTypes.length; i++){
                if(i == roomTypes.length-1){
                    System.out.printf("%s (%2d) \n", meal.getMealType(), roomTypes[i]);
                }else{
                    System.out.printf("%s (%3d)   ", room.getRoomType(i), roomTypes[i]);
                }
            }
            System.out.printf("Available cashback = %-13d>> Total room price++  = %11.2f     with service charge and VAT\n", cashBack, totalRoomPrice);
            System.out.printf("                                  >> Total meal price    = %11.2f\n",totalMealPrice);
            System.out.printf("                                  >> Total bill          = %11.2f     redeem = %-8d\n", totalBill, redeem);
            System.out.printf("                                  >> Final bill          = %11.2f     cashback for next booking = %-8d\n\n", finalBill, newCashback);
        }

        //Room summary
        System.out.println("===== Room Summary =====");
        room.sortRoom();
        room.printResult();

        //Meal summary
        System.out.println("===== Meal Summary =====");
        meal.printResult();
    }

    //function that add booking and customer in to Class Booking and Customer
    public static void addBooking(Scanner scan,String bookingPath, Booking booking) throws Exception {

        System.out.println("Read booking data from file " + bookingPath);
        System.out.println();

        // Loop through the file and add booking data
        while (scan.hasNext()) {

            String line = scan.nextLine();
            String[] col = line.split(",");

            try {
                // Catch InvalidInputException
                if (Integer.parseInt(col[0].trim()) < 0) {
                    throw new InvalidInputException("For input: " + Integer.parseInt(col[0].trim()));
                }
                if (Integer.parseInt(col[2].trim()) < 0) {
                    throw new InvalidInputException("For input: " + Integer.parseInt(col[2].trim()));
                }
                for (int i = 3; i <=7 ;i++) {
                    if (Integer.parseInt(col[i].trim()) < 0) {
                        throw new InvalidInputException("For input: " + col[i].trim());
                    }
                }

                // Assign value to variable
                int bookingID = Integer.parseInt(col[0].trim());
                String customerName = col[1].trim();
                int night = Integer.parseInt(col[2].trim());

                int[] roomTypes = new int[5];
                roomTypes[0] = Integer.parseInt(col[3].trim());
                roomTypes[1] = Integer.parseInt(col[4].trim());
                roomTypes[2] = Integer.parseInt(col[5].trim());
                roomTypes[3] = Integer.parseInt(col[6].trim());
                roomTypes[4] = Integer.parseInt(col[7].trim());
                booking.addBooking(bookingID, customerName, night, roomTypes);
            } catch(Exception e){
                System.out.println(e);
                System.out.println("[" + line + "]  --> skip this booking");
                System.out.println();
            }
        }
    }


    // Function that add room and meal in to Class Room and Meal
    public static void addHotel(Scanner scan,String hotelPath, Room room, Meal meal) throws Exception {

        System.out.println("Read hotel data from file " + hotelPath);
        System.out.println();
        int roomCount = 0;
        int mealCount = 0;

        // Loop through the file and add room and meal
        while(scan.hasNext()) {
            String line = scan.nextLine();
            String[] col = line.split(",");

            // Add Room or Meal
            if (line.startsWith("R")) {
                roomCount++;
                String roomType = col[1].trim();
                double roomPrice = Double.parseDouble(col[2].trim());
                room.addRoom(roomType, roomPrice);
            } else if (line.startsWith("M")){
                mealCount++;
                String mealType = col[1].trim();
                String priceStr = col[2].trim();
                double mealPrice = Double.parseDouble(priceStr);
                meal.addMeal(mealType, mealPrice);
            }else {
                throw new Exception("Invalid item type");
            }
        }

        // RoomCount validation
        if (roomCount != 4) { throw new Exception("RoomTypes must have exact 4 types"); }

        // MealCount validation
        if(mealCount != 1){ throw new Exception("MealTypes must have exact 1 type"); }
        // Print data
        room.print();
        meal.print();
        System.out.println();
    }

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String errorMessage) {
            super(errorMessage);
        }
    }
}