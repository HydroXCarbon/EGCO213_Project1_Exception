//6513135 Purin Pongpanich
//6513161 Jarupat Chodsitanan
//6513163 Chalisa Buathong
package Project1_135.module;

public class Meal {
    // Create variable
    private String mealType;
    private double price;
    private double totalSaleInCash;
    private int totalSaleInUnit;

    // Add meal with its price
    public void addMeal (String mealType, double price ) throws MealException{
        if (price < 0) {
            throw new MealException(String.format("Price cannot be negative: %.2f", price));
        }

        this.mealType = mealType;
        this.price = price;
    }

    //Add totalSaleInCash
    public void addTotalSaleInCash(double totalSaleInCash) {
        this.totalSaleInCash += totalSaleInCash;
    }

    //Add totalSaleInUnit
    public void addTotalSaleInUnit(int totalSaleInUnit) {
        this.totalSaleInUnit += totalSaleInUnit;
    }

    // Access MealType
    public String getMealType(){
        return mealType;
    }

    // Access too MealPrice
    public double getPrice(){
        return price;
    }

    // Print meal data
    public void print(){
        String output = String.format("%-21s rate =%,10.2f      rate++ =%,10.2f", getMealType(), getPrice(), getPrice());
        System.out.println(output);
    }

    // Print meal result data
    public void printResult(){
        String output = String.format("%-19s total sales = %,6d heads %,15.2f Baht",mealType,totalSaleInUnit,totalSaleInCash);
        System.out.println(output);
    }

    // Custom Meal Exception
    public static class MealException extends Exception{
        public MealException(String errorMessage){
            super(errorMessage);
        }
    }
}
