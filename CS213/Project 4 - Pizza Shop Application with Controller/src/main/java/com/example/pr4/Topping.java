package com.example.pr4;


public enum Topping {
    SAUSAGE("Sausage"), BBQ_CHICKEN("BBQ Chicken"), BEEF("Beef"), HAM("Ham"),
    PEPPERONI("Pepperoni"), GREEN_PEPPER("Green Pepper"), ONION("Onion"), MUSHROOM("Mushroom"),
    PINEAPPLE("Pineapple"), BLACKOLIVES("Black Olives"), PROVOLONE("Provolone"), SPINACH("Spinach"),
    CHEDDAR("Cheddar");

    private final String name;

    Topping(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
