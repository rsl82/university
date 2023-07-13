package project1;

/**
 * Class that contains location of the gyms.
 * @author Ryan S. Lee, Elliott Ng
 */

public enum Location {
    BRIDGEWATER ("08807", "SOMERSET"),
    EDISON ("08837","MIDDLESEX") ,
    FRANKLIN ("08873","SOMERSET"),
    PISCATAWAY ("08854","MIDDLESEX"),
    SOMERVILLE ("08876","SOMERSET");

    private final String zipCode;
    private final String county;

    /**
     * The constructor to use in this class, not for use in other classes.
     * @param zipCode Zipcode of the location.
     * @param county County of the location.
     */
    Location(String zipCode, String county)
    {
        this.zipCode = zipCode;
        this.county = county;
    }

    /**
     * Getter method to get zipcode of the location in other classes.
     * @return zipcode of the location.
     */
    public String zipcodeGetter() {
        return this.zipCode;
    }

    /**
     * Getter method to get county of the location in other classes.
     * @return county of the location.
     */
    public String countyGetter() {
        return this.county;
    }
}

