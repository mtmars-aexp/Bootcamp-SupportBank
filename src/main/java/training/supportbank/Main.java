package training.supportbank;
import java.util.*;
import java.io.*;
import java.io.File;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.LocalDate;
import java.time.format.*;
import java.math.BigDecimal;

import org.json.simple.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;


public class Main {

    //1. Read file and populate transactionList accordingly (with a switch statement for the file extension (.csv and .json supported).
    //2. Populate employeeList based on the information in the transactionList. Do not add employees if they already exist.
    //3. Update the totalSent and totalReceived of each employee based on the information in transactionList.
    //4. Output the balance of each employee.


    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException {

        //Variable initialisation and setup.
        List<Employee> employeeList = new ArrayList<>();
        List<IOU> transactionList = new ArrayList<>();
        String fileName = "DodgyTransactions2015.csv";


        //Main methods.
        populateTransactionList(transactionList, fileName);

        populateEmployeeList(employeeList, transactionList);

        updateEmployeeBalance(employeeList, transactionList);

        outputEmployeeBalance(employeeList);

        LOGGER.debug("Program successfully completed.");


    }

    private static void populateTransactionList(List<IOU> transactionList, String fileName) throws IOException {

        //USE: Populate transactionList with specific parsing methods based on the file's extension.

        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

        if(fileExtension.equals(".csv")) {
            LOGGER.debug("Filetype is .csv");
            List<String> linesList = Files.readAllLines((Paths.get(fileName)), Charset.forName("utf-8"));
            linesList.remove(0);

            for (int i = 0; i < linesList.size(); i++) {
                String[] columns = splitLine(linesList, i);
                if(currentLineIsValid(columns, i)) {
                    transactionList.add(new IOU(columns[0], columns[1], columns[2], columns[3], new BigDecimal(columns[4])));
                } else {
                    LOGGER.error("Skipping line " + i);
                }
            }
        } else if(fileExtension.equals(".json")){
            LOGGER.debug("Filetype is .json");
        } else {
            LOGGER.error("File type not supported. Use .json or .csv");
            System.exit(216);
        }

    }

    private static void populateEmployeeList(List<Employee> employeeList, List<IOU> transactionList){

        //USE: Populate employeeList with content from linesList

        LOGGER.debug("Beginning employeeList population.");

        for(int i = 0; i < transactionList.size(); i++){
            if (!checkEmployeeExists(employeeList, transactionList.get(i).getSenderName())) {
                employeeList.add(new Employee(transactionList.get(i).getSenderName()));
            }
            if (!checkEmployeeExists(employeeList, transactionList.get(i).getRecipientName())) {
                employeeList.add(new Employee(transactionList.get(i).getRecipientName()));
            }
            }
    }

    private static Boolean checkEmployeeExists(List<Employee> employeeList, String nameToCheck){

        //USE: Check employeeList to see if any entries match the current name.

        for(int i = 0; i < employeeList.size(); i++){
            if(employeeList.get(i).getName().equals(nameToCheck)){
                LOGGER.debug(nameToCheck + " already exists in the employeeList list. Not adding.");
                return true;
            }
        }
        LOGGER.debug(nameToCheck + " doesn't exist in employeeList. Adding to list.");
        return false;
    }

    private static void updateEmployeeBalance(List<Employee> employeeList, List<IOU> transactionList){

        //USE: Update the balance of the account of every employee on the list based on the amount listed in the transactional records..

        LOGGER.debug("Beginning employee balance updates.");

        //Loop through every entry in transactionList to find the amount.
        for(int i = 0; i < transactionList.size(); i++){
            //Loop through every entry in employeeList until you find a matching name (For senders).
            for(int j = 0; j < employeeList.size(); j++){
                if(employeeList.get(j).getName().equals(transactionList.get(i).getSenderName())){
                    employeeList.get(j).incrementTotalSent((transactionList.get(i).getAmount()));
                    LOGGER.debug("Incremented " + employeeList.get(j).getName() + "'s total sent by " + transactionList.get(i).getAmount());
                    break;
                }
            }

            //Loop through every entry in employeeList until you find a matching name (For receivers).
            for(int j = 0; j < employeeList.size(); j++){
                if(employeeList.get(j).getName().equals(transactionList.get(i).getRecipientName())){
                    employeeList.get(j).incrementTotalReceived(transactionList.get(i).getAmount());
                    LOGGER.debug("Incremented " + employeeList.get(j).getName() + "'s total received by " + transactionList.get(i).getAmount());
                    break;
                }
            }

        }

    }

    private static String[] splitLine(List<String> linesList, int i){
        return linesList.get(i).split(",");
    }

    private static void outputEmployeeBalance(List<Employee> employeeList){

        //USE: Output all balances from the employees on the employeeList.

        LOGGER.debug("Beginning employee balance output.");

        for(int i = 0; i < employeeList.size(); i++){
            System.out.println(employeeList.get(i).getName());
            System.out.println(employeeList.get(i).getBalance());
        }
    }

    private static void validateTransactionList(List<IOU> transactionList) {

        //USE: Validates each entry in the transaction list. Useless because it validates them after they've already been added.
        //TODO: Remove this garbage.


        for(int i = 0; i < transactionList.size(); i++){
            LOGGER.debug("Validating line " + i);

            boolean errorFound = false;

            //Column 1: Date validation.
            try{
                LocalDate.parse(transactionList.get(i).getDate(),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch (Exception e){
                errorFound = true;
                LOGGER.error("Date on line " + i + " is invalid as '" + transactionList.get(i).getDate() + "' Skipping and removing line.");
            }

            //Column 5: Amount validation.
            try{
                BigDecimal validate = new BigDecimal(transactionList.get(i).getAmount().toBigInteger());
            }
            catch (Exception e){
                errorFound = true;
                LOGGER.error("Amount on line " + i + " is invalid as '" + transactionList.get(i).getAmount() + "' Skipping and removing line.");
            }
            if(errorFound){
                transactionList.remove(i);
            } else {
                LOGGER.debug("Line successfully validated. No errors found.");
            }

        }



    }

    private static boolean currentLineIsValid(String[] columns, int currentLine){


        boolean isValid = true;



        //Column 1: Date validation.
        try{
            LocalDate.parse(columns[0],DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch (Exception e){
            isValid = false;
            LOGGER.error("Date on line " + currentLine + " is invalid as '" + columns[0] + "' Skipping and removing line.");
        }

        //Column 5: Amount validation.
        try{
            new BigDecimal(columns[4]);
        }
        catch (Exception e){
            isValid = false;
            LOGGER.error("Amount on line " + currentLine + " is invalid as '" + columns[4] + "' Skipping and removing line.");
        }

        return isValid;
    }






}