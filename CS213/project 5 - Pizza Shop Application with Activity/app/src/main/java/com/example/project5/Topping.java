package com.example.project5;


public enum Topping {
    SAUSAGE("Sausage"), BBQ_CHICKEN("BBQ Chicken"), BEEF("Beef"), HAM("Ham"),
    PEPPERONI("Pepperoni"), GREEN_PEPPER("Green Pepper"), ONION("Onion"), MUSHROOM("Mushroom"),
    PINEAPPLE("Pineapple"), BLACKOLIVES("Black Olives"), PROVOLONE("Provolone"), SPINACH("Spinach"),
    CHEDDAR("Cheddar");

    private final String name;

    /**
     * Constructor for Enum Topping
     * @param name String name of the topping.
     */
    Topping(String name) {
        this.name = name;
    }

    /**
     * return the name of the topping.
     * @return String of the topping.
     */
    @Override
    public String toString() {
        return this.name;
    }

}
