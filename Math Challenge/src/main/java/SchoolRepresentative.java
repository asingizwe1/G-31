import java.io.BufferedReader;
import java.io.FileReader;//For reading character files.
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;




public class SchoolRepresentative {
    private String name;
    private String username;
    private String email;
    private String password;
    private String school_reg_no;

    //constructor
    public SchoolRepresentative(String name, String username, String email, String password, String schoolRegNo) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.school_reg_no = schoolRegNo;

    }
    //Methods to set the fields of the SchoolRepresentative.
    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSchool_reg_no(String school_reg_no) {
        this.school_reg_no = school_reg_no;
    }
    //Methods to get the values of the fields.
    //getters
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSchool_reg_no() {
        return school_reg_no;
    }

    //log school representative into system
    public void login(String username, String password){
        //Intended for school representatives to log in, currently not implemented.
    }
    //school representative can view pending applicants int the java file for their school
    public static void viewApplicants(String schoolName, PrintWriter out){
//viewApplicants method reads the contents of the file (e.g., "applicants.txt")
        //This method reads applicants from a file and prints their details using a PrintWriter.
        String filename = schoolName + ".txt";
        //schoolName parameter is used to construct the filename from which the applicants are read. The value for schoolName should be provided when calling the viewApplicants method
         // File name based on school name
       //Each line read from the file is printed to the PrintWriter instance (out), which outputs to the CLI.
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
//the out is our print writer object
            while ((line = br.readLine()) != null) {
                //Read each line of the file
              out.println(line);// // Print the line using PrintWriter
            ////////////start///////////////////
                ////////////start///////////////////

                ////////////////////////////////
            }
            //            out.println();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


    //school representative accepts or rejects applicant
    public static void confirmApplicant(String username, String accept,PrintWriter out) {
        //processes an applicant's confirmation based on the representative's decision.
        ArrayList<pupil_detail> applicants = pupil_detail.addToArrayList("applicants");
        //above: to get a list of all applicants.
        // System.out.println(applicants);
        boolean found = false;//initially set to false
        email emailInstance = new email(); // Create an instance of the email class
// It iterates over the list to find a pupil with the specified username.
        for (pupil_detail pupil : applicants) {
            //System.out.println(pupil.getUsername());
            System.out.println(username);
            // If found, it updates the pupil's status in the database using Model.updatePupil(pupil) or Model.updateRejected(pupil).
            if (pupil.getUsername().equals(username)) {
                found = true;
                System.out.println("found");
                if (accept.equals("yes")) {

                    Model.updatePupil(pupil);
                    pupil_detail.deleteFromFile(username);
                    out.println("You have confirmed " + pupil.getName());
//////////////////////////start///////////
                    // Handle image processing and database storage
                    try {
                        // Read image file into byte array
                        File file = new File(pupil.getImage());
                        FileInputStream fis = new FileInputStream(file);
                        byte[] imageBytes = new byte[(int) file.length()];
                        fis.read(imageBytes);
                        fis.close();

                        // Store the image in the directory and update the database with the file path
                        Model.storeImageInDirectory(pupil.getUsername(), imageBytes);
                    } catch (IOException e) {
                        System.err.println("Error processing image: " + e.getMessage());
                    }
                    emailInstance.sendAcceptanceEmail(pupil.getEmail(), pupil.getUsername());
                    //send email to pupil
                } else if (accept.equals("no")) {
                    Model.updateRejected(pupil);
                    pupil_detail.deleteFromFile(username);
                    /////////////start////////
                    ///////////start/////////
                  /* pupil_detail.submitImageToDatabase(username);*/
                    out.println("You have rejected " + pupil.getName());
                    emailInstance.sendEmailNotification(pupil.getSchoolRegNo(), pupil.getUsername(), true);
                    //send email to pupil
                }
            } else {
                System.out.println("confirming ...");
            }
        }
        if (!found) {
            out.println("wrong pupil username");
        }
    }
/*

/////////////start/////////////

///////////////////////

*/
///////////////start///////////////
 public static void registerRep_(String[] req, PrintWriter out){
    if (req.length!=7){
        out.println("Missing parameters");
        out.println("");// helps break out of inner loop
    }else {
        String username = req[1];
        String name = req[2] +" "+ req[3];
        String email = req[4];
        String password = req[5];
        String school_reg_no = req[6];

        SchoolRepresentative rep = new SchoolRepresentative(username, name, email, password, school_reg_no);
        System.out.println("Registering rep: " + username); // Debug statement
        if (!Model.checkRepRegNo(rep)) {
        out.println("Administrator didnt verify you");
        out.println("");
    } else {
            Model.registerRepresentative(rep);
        /////////////start////////////
         //   email emailInstance = new email();
       // emailInstance.sendRepresentativeRegistrationEmail(rep.getSchool_reg_no(), rep.getUsername(),  true);
       // Corrected call to send email to school representative
        out.println("REGISTERED BY THE ADMINISTRATOR");
        out.println("");
    }
}
}
}
