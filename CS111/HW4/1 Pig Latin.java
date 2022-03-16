
	public class PigLatin {

    /*public static void main(String[] args) {

        String x = "";
        x = IO.readString();



        System.out.println(translate(x));


    }
 */

    public static String translate(String original)
    {

        original = original.toLowerCase();
        String a = "";

        if( original.charAt(0) == 'a' || original.charAt(0) == 'e' || original.charAt(0) == 'i' || original.charAt(0) == 'o' || original.charAt(0) == 'u')
            a = original + "vai";
        else
            a = original.substring(1) + original.substring(0,1) + "ai";

        return a;
    }
}
