public class DoOp {
    public static String operate(String[] args) {
        if(args == null) {
            return "Error";
        }
        if (args.length == 3) {
            int firstNumb = 0;
            int secondNumb = 0; 
            try {
                firstNumb = Integer.parseInt(args[0]); 
                secondNumb = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                return "Invalid number";
            }
            switch (args[1]) {
                case "+":
                   return String.valueOf(firstNumb + secondNumb);
                case "-":
                    return String.valueOf(firstNumb - secondNumb);
                case "*":
                    return String.valueOf(firstNumb * secondNumb);
                case "%":
                    if (secondNumb == 0) {
                        return "Error";
                    }
                    return String.valueOf(firstNumb % secondNumb);
                case "/":
                    if (secondNumb == 0) {
                        return "Error";
                    }
                    return String.valueOf(firstNumb / secondNumb);       
                default:
                    return "Error"; 
            }
        } else {
            return "Error";
        }

    }

}