package uj.wmii.pwj.spreadsheet;

public class Spreadsheet {

    public String[][] calculate(String[][] input){
        int Height = input.length;
        int Width = input[0].length;
        int[][] values = new int[Height][Width];
        String[][] output = new String[Height][Width];
        
        for(int row=0; row<Height; row++){
            for(int col=0; col<Width; col++){
                calculateCell(row, col, input, values, output);
            }
        }

        return output;
    }

    private void calculateCell(int row, int col, String[][] input, int[][] values, String[][] output){// fills the output and values matrix
        if(output[row][col] != null) return;// the cell is already calculated

        if(input[row][col] == null || input[row][col].isEmpty()){
            values[row][col] = 0;
            output[row][col] = "0";
        }

        if(input[row][col].charAt(0) == '$'){ // reference
            Pair refCellCoords = getCoordinates(input[row][col]);// gets the indexes of the referenced cell
            if(refCellCoords != null){
                calculateCell(refCellCoords.first, refCellCoords.second, input, values, output);// calculate the referenced cell first
                values[row][col] = values[refCellCoords.first][refCellCoords.second];
                output[row][col] = output[refCellCoords.first][refCellCoords.second];
            }
            else {// invalid reference
                values[row][col] = 0;
                output[row][col] = "0";
            }
        }

        else if(input[row][col].charAt(0) == '='){// function
            String func = input[row][col].substring(1,4);
            String par1Str, par2Str;
            int par1, par2;
            int comaIndex = input[row][col].indexOf(',');
            int endIndex = input[row][col].indexOf(')');
            if(comaIndex == -1 || endIndex == -1 || comaIndex > endIndex || input[row][col].charAt(4) != '('){// checking the structure
                values[row][col] = 0;   // bad function // return function as 0
                output[row][col] = "0";
                return;
            }
            par1Str = input[row][col].substring(5,comaIndex);
            par2Str = input[row][col].substring(comaIndex+1,endIndex);

            // getting parameter1
            if(par1Str.charAt(0) == '$'){// reference
                Pair par1Coords = getCoordinates(par1Str);
                if(par1Coords != null){
                    calculateCell(par1Coords.first, par1Coords.second, input, values, output);// calculate the referenced cell first
                    par1 = values[par1Coords.first][par1Coords.second];
                }
                else {// invalid reference // return function as 0
                    values[row][col] = 0;
                    output[row][col] = "0";
                    return;
                }
            }
            else{// just a number
                try {par1 = Integer.parseInt(par1Str);}
                catch (NumberFormatException e) {// bad number // return function as 0
                    values[row][col] = 0;
                    output[row][col] = "0";
                    return;
                }
            }

            // getting parameter2
            if(par2Str.charAt(0) == '$'){// reference
                Pair par2Coords = getCoordinates(par2Str);
                if(par2Coords != null){
                    calculateCell(par2Coords.first, par2Coords.second, input, values, output);// calculate the referenced cell first
                    par2 = values[par2Coords.first][par2Coords.second];
                }
                else {// invalid reference // return function as 0
                    values[row][col] = 0;
                    output[row][col] = "0";
                    return;
                }
            }
            else{// just a number
                try {par2 = Integer.parseInt(par2Str);}
                catch (NumberFormatException e) {// bad number // return function as 0
                    values[row][col] = 0;
                    output[row][col] = "0";
                    return;
                }
            }

            //apply the function
            int res;
            switch(func){
                case "ADD": res = par1 + par2; break;
                case "SUB": res = par1 - par2; break;
                case "MUL": res = par1 * par2; break;
                case "DIV": res = (par2 != 0) ? par1 / par2 : 0; break;// return 0 if divide by 0
                case "MOD": res = (par2 != 0) ? par1 % par2 : 0; break;//
                default: res = 0; break;// invalid function, return 0
            }

            values[row][col] = res;
            output[row][col] = Integer.toString(res);
        }

        else{// just a number
            try {
                values[row][col] = Integer.parseInt(input[row][col]);
                output[row][col] = input[row][col];
            }
            catch (NumberFormatException e) {// bad number
                values[row][col] = 0;
                output[row][col] = "0";
            }
        }
    }

    private Pair getCoordinates(String address){
        if(address == null || address.isEmpty() || address.charAt(0) != '$') return null;
        int length = address.length();
        int index = 1;
        int col = 0;
        int row = 0;
        for(; index<length; index++){
            if('A' <= address.charAt(index) && address.charAt(index) <= 'Z')
                col = col * 26 + (address.charAt(index) - 64);// 26 letters, 'A' = 65
            else break;
        }
        for(; index<length; index++){
            if('0' <= address.charAt(index) && address.charAt(index) <= '9')
                row = row * 10 + (address.charAt(index) - 48);// 10 digits, '0' = 48
            else return null;// invalid reference
        }

        if(row == 0 || col == 0) return null;
        return new Pair(row-1, col-1);
    }

    private class Pair {
        public int first;
        public int second;
        public Pair(int first, int second) {this.first = first; this.second = second;}
    }
}
