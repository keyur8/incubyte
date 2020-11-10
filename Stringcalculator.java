import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Scanner;

class StringCalcShould {
    public void return_0_when_input_is_empty() {
        assertEquals("0", given(""));
    }
    public void return_3_when_input_is_1_2() {
        assertEquals("3", given("1,2"));
    }
    public void sum_floats_and_return_float() {
        assertEquals("6.6", given("2.2,4.4"));
    }
    public void treat_newLine_as_a_delimiter() {
        assertEquals("6", given("1\n2,3"));
    }
    public void ignor_number_bigger_then_Thousand(){
        assertEquals("1,2,2900 == 3" , given("//@@\n1@@2@@2900"));
    }
    public void return_error_msg_when_newLine_at_invalid_position() {
        assertEquals("Number expected but '\n' found at position 6.", given("1,2,5,\n3"));
    }
    public void return_error_msg_when_delimiter_at_last_position() {
        assertEquals("Number expected but EOF found.", given("2,3,4.2,"));
    }
    public void return_correct_sum_when_custom_delimiter_is_used() {
        assertEquals("3", given("//;\n1;2"));
        assertEquals("3", given("//|\n1|2"));
        assertEquals("8", given("//@@\n1@@2@@5"));
        assertEquals("5", given("//sep\n2sep3"));
    }
    public void return_string_of_negative_numbers_when_negative_numbers_are_used_as_input() {
        assertEquals("Negative not allowed: -1", given("-1,2"));
        assertEquals("Negative not allowed: -4,-5", given("2,-4,-5"));
    }

    private static String given(String number) {
        StringCalc stringCalc = new StringCalc();
        return stringCalc.sum(number);
    }

}

class StringCalc
{
    private float result;
    private String customDelimiter;
    private static final String DEFAULT_DELIMITER = ",";
    private static final String NEWLINE = "\\n";
    private static final String CUSTOM_DELIMITER_PREFIX = "/";
    private static final String CUSTOM_DELIMITER_SUFFIX = NEWLINE;

    StringCalc() 
	{
        result = 0;
        customDelimiter = "";
    }

    public String sum(String numbers) 
	{
        if (numbers.isEmpty())
            return String.format("%.0f", result);

        if (isInvalidLastCharacterIn(numbers))
            return "Number expected but EOF found.";

        if (numbers.startsWith(CUSTOM_DELIMITER_PREFIX))
            numbers = setCustomDelimiter(numbers);

        if (isNewlineAtInvalidPositionIn(numbers))
            return String.format("Number expected but '\n' found at position %d.", numbers.lastIndexOf('\n'));

        if (containsNegative(numbers).length() > 0)
            return String.format("Negative not allowed: %s", containsNegative(numbers));

        calculateSumOf(getStringArray(numbers));

        return hasDecimalPlaces() ? printFloat(numbers) : printInteger(numbers);
    }

    private boolean isInvalidLastCharacterIn(String numbers) {
        return Character.digit(numbers.charAt(numbers.length() - 1), 10) < 0;
    }

    private boolean isNewlineAtInvalidPositionIn(String numbers) {
        return numbers.lastIndexOf(NEWLINE) > numbers.lastIndexOf(DEFAULT_DELIMITER);
    }

    private StringBuilder containsNegative(String numbers) {
        StringBuilder negativeNumbers = new StringBuilder();

        for (String number : getStringArray(numbers))
            if (Float.valueOf(number) < 0) negativeNumbers.append(number + ",");

        boolean commaIsLastChar = negativeNumbers.length() > 0 && negativeNumbers.charAt(negativeNumbers.length() -1) == ',';

        return commaIsLastChar ? negativeNumbers.deleteCharAt(negativeNumbers.length() - 1)
                               : negativeNumbers;
    }

    private String setCustomDelimiter(String numbers) {
        int customDelimiterStart = numbers.lastIndexOf(CUSTOM_DELIMITER_PREFIX) + 1;
        int customDelimiterEnd = numbers.indexOf(CUSTOM_DELIMITER_SUFFIX);

        customDelimiter = numbers.substring(customDelimiterStart, customDelimiterEnd);
        return numbers.substring(customDelimiterEnd + 2).replace(customDelimiter, DEFAULT_DELIMITER);
    }

    private String[] getStringArray(String numbers) {
        return numbers.replace(NEWLINE, DEFAULT_DELIMITER).split(DEFAULT_DELIMITER);
    }

    private void calculateSumOf(String[] numbers) {
        for (String number : numbers){
          if(Float.parseFloat(number)<1000)
            result = Float.sum(result, Float.parseFloat(number));
        }
    }

    private boolean hasDecimalPlaces() {
        return result % 1 != 0;
    }

    private String printFloat(String numbers) {
        return ("\""+numbers+"\" == "+Float.toString((float) (Math.round(result * 100.0) / 100.0)));
    }

    private String printInteger(String numbers) {
        return ("\""+numbers+"\" == "+String.valueOf((int) result));
    }
}
class StringCalculator
{
	public static void main(String[] args)
		{
		  System.out.println("Enter test case : ");
		  String str;
		  str = (new Scanner(System.in)).next();
		  System.out.println((new StringCalc()).sum(str));
		}
}
