package com.example.project3cs213;
/**
 Location enum class to contain enum for the gym locations
 @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public enum Location {
    SOMERVILLE ("08876", "SOMERSET"),
    BRIDGEWATER ("08807", "SOMERSET"),
    EDISON ("08837", "MIDDLESEX"),
    PISCATAWAY ("08854", "MIDDLESEX"),
    FRANKLIN ("08873", "SOMERSET");
    private final String zipCode;
    private final String county;
    /**
     Location constructor to initialize a Location instance for each of the gym location enums.
     @param zipCode the zipCode for the gym locations.
     @param county the county for the gym locations.
     */
    Location(String zipCode, String county) {
        this.zipCode = zipCode;
        this.county = county;
    }
    /**
     Get the zipCode of the location.
     @return zipCode of the Location object.
     */
    public String getZipCode() {
        return zipCode;
    }
    /**
     Get the county of the location.
     @return county of the Location object.
     */
    public String getCounty() {
        return county;
    }
    /**
     Compare which location comes first than the other in the ascending order sequence.
     If two members have the same county, order by their zip codes.
     @param location the Location instance that is being compared with the current Location instance.
     @return Compare.EQUAL if locations are equal, Compare.MORETHAN if the current location object comes after parameter location object, Compare.LESSTHAN if otherwise.
     */
    public int compare(Location location) {
        if (location.getCounty().toLowerCase().equals(getCounty().toLowerCase())) { // if counties are the same check zipcodes
            if (Integer.parseInt(location.getZipCode()) > Integer.parseInt(getZipCode())) // checks zipcodes
                return Compare.MORETHAN;
            if (Integer.parseInt(location.getZipCode()) < Integer.parseInt(getZipCode()))
                return Compare.LESSTHAN;
            return Compare.EQUAL;
        } //if counties are not the same then checks counties
        if (location.getCounty().toLowerCase().compareTo(getCounty().toLowerCase()) > 0) {
            return Compare.MORETHAN;
        }
        if (location.getCounty().toLowerCase().compareToIgnoreCase(getCounty()) < 0) {
            return Compare.LESSTHAN;
        }
        return Compare.EQUAL;
    }
    /**
     Override the toString method and print the instance variables.
     Print the city name, zip code, and county.
     @return string containing the city name, zip code, and county.
     */
    @Override
    public String toString() {
        return this.name() + ", " + this.zipCode + ", " + this.county;
    }
}
