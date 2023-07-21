import java.sql.*; 
import java.util.Scanner;


class HW3{  
    public static void main(String args[]){  
    
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con=DriverManager.getConnection(  
            args[0],args[1],args[2]);  
        

            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to the university database. Queries available:");
            System.out.println("1. Search students by name.");
            System.out.println("2. Search students by year.");
            System.out.println("3. Search for students with a GPA >= threshold.");
            System.out.println("4. Search for students with a GPA <= threshold.");
            System.out.println("5. Get department statistics.");
            System.out.println("6. Get class statistics.");
            System.out.println("7. Execute an abitrary SQL query.");
            System.out.println("8. Exit the application.");
            String line = "";
        while(true) {
            System.out.println("Which query would you like to run (1-8)?");
            line = sc.nextLine();
            
            if(line.equals("1")) {
                System.out.println("Please enter the name.");
                line = sc.nextLine();
                String sql = "SELECT * FROM Students WHERE lower(first_name) LIKE ? OR lower(last_name) LIKE ?";
                PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, "%" + line.toLowerCase() + "%");
                stmt.setString(2, "%" + line.toLowerCase() + "%");
                ResultSet rs = stmt.executeQuery();

                studentPrinter(rs,con);
                

            }

            else if(line.equals("2")) {
                System.out.println("Please enter the year.");
                line = sc.nextLine();
                
                if(line.equals("Fr")) {
                    String sql = "SELECT s.id, s.first_name, s.last_name FROM Students s JOIN HasTaken t ON s.id = t.sid JOIN Classes c ON t.name = c.name WHERE t.grade != 'F' GROUP BY s.id HAVING SUM(c.credits) <= 29";
                    PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stmt.executeQuery();
                    studentPrinter(rs, con);
                }
                else if(line.equals("So")) {
                    String sql = "SELECT s.id, s.first_name, s.last_name FROM Students s JOIN HasTaken t ON s.id = t.sid JOIN Classes c ON t.name = c.name WHERE t.grade != 'F' GROUP BY s.id HAVING SUM(c.credits) >= 30 AND SUM(c.credits) <=59";
                    PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stmt.executeQuery();
                    studentPrinter(rs, con);

                }
                else if(line.equals("Ju")) {
                    String sql = "SELECT s.id, s.first_name, s.last_name FROM Students s JOIN HasTaken t ON s.id = t.sid JOIN Classes c ON t.name = c.name WHERE t.grade != 'F' GROUP BY s.id HAVING SUM(c.credits) >= 60 AND SUM(c.credits) <= 89";
                    PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stmt.executeQuery();
                    studentPrinter(rs, con);

                }
                else if(line.equals("Sr")) {
                    String sql = "SELECT s.id, s.first_name, s.last_name FROM Students s JOIN HasTaken t ON s.id = t.sid JOIN Classes c ON t.name = c.name WHERE t.grade != 'F' GROUP BY s.id HAVING SUM(c.credits) >= 90";
                    PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stmt.executeQuery();
                    studentPrinter(rs, con);
                }
            }

            else if(line.equals("3")) {
                System.out.println("Please enter the threshold.");
                line = sc.nextLine();
                String sql = "SELECT s.id, s.first_name, s.last_name, SUM(c.credits * CASE t.grade WHEN 'A' THEN 4.0 WHEN 'B' THEN 3.0 WHEN 'C' THEN 2.0 WHEN 'D' THEN 1.0 ELSE 0.0 END) / SUM(c.credits) AS GPA FROM Students S JOIN HasTaken t ON s.id=t.sid JOIN Classes c ON t.name=c.name GROUP BY s.id HAVING GPA >= ?";
                PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, line);
                ResultSet rs = stmt.executeQuery();

                studentPrinter(rs, con);
            }
            
            else if(line.equals("4")) {
                System.out.println("Please enter the threshold.");
                line = sc.nextLine();
                String sql = "SELECT s.id, s.first_name, s.last_name, SUM(c.credits * CASE t.grade WHEN 'A' THEN 4.0 WHEN 'B' THEN 3.0 WHEN 'C' THEN 2.0 WHEN 'D' THEN 1.0 ELSE 0.0 END) / SUM(c.credits) AS GPA FROM Students S JOIN HasTaken t ON s.id=t.sid JOIN Classes c ON t.name=c.name GROUP BY s.id HAVING GPA <= ?";
                PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, line);
                ResultSet rs = stmt.executeQuery();

                studentPrinter(rs, con);
            }

            else if(line.equals("5")) {
                System.out.println("Please enter the department.");
                line=sc.nextLine();
                String sql = "SELECT DISTINCT sid FROM (SELECT sid FROM Majors WHERe dname = ? UNION SELECT sid FROM Minors Where dname = ?) AS m";
                PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, line);
                stmt.setString(2, line);
                ResultSet rs = stmt.executeQuery();


                String temp = "SELECT COUNT(DISTINCT sid) FROM (SELECT sid FROM Majors WHERe dname = ? UNION SELECT sid FROM Minors Where dname = ?) AS m";
                PreparedStatement tempp = con.prepareStatement(temp,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                tempp.setString(1,line);
                tempp.setString(2, line);
                ResultSet temprs = tempp.executeQuery();
                int numstu = 0;
                if(temprs.next()) {
                    numstu = temprs.getInt(1);
                    System.out.println("Num students: " + numstu);
                }
                
                double GPAs = 0.0;
                
                while(rs.next()) {
                    String id = rs.getString("sid");
                    String sql2 = "Select * FROM HasTaken WHERE sid = ?";
                    PreparedStatement stmt2 = con.prepareStatement(sql2);
                    stmt2.setString(1,id);
                    ResultSet rs2 = stmt2.executeQuery();
                    double gradeNum = 0.0;
                    int credits = 0;
                    double GPA = 0.0;
                    while(rs2.next()) {
                        String grades = rs2.getString("grade");
                        String cName = rs2.getString("name");
                        String sql3 = "Select credits FROM Classes WHERE name = ?";
                        PreparedStatement stmt3 = con.prepareStatement(sql3);
                        stmt3.setString(1, cName);
                        ResultSet rs3 = stmt3.executeQuery();
                        if(rs3.next()) {
                            int credit = rs3.getInt("credits");
                            credits = credits + credit;
                            gradeNum = gradeNum + (credit * gradeConverter(grades));
                        }

                    }
                    GPA = gradeNum / credits;
                    GPAs = GPAs + GPA;
                   //System.out.println("ID: "+ id + "GPA: "+ GPA);
                }
                //System.out.println("GPAs " +GPAs);
                GPAs  = GPAs/numstu;
                System.out.println("Average GPA: "+ String.format("%.3f",GPAs));
            }

            else if(line.equals("6")) {
                System.out.println("Please enter the class name.");
                line = sc.nextLine();
                String sql = "SELECT COUNT(*) FROM IsTaking where name=?";
                PreparedStatement stmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, line);
                ResultSet rs = stmt.executeQuery();

                rs.next();
                System.out.println(rs.getInt(1) + " students currently enrolled");
                System.out.println("Grades of previous enrollees:");
                String sql2 = "SELECT grade, COUNT(*) AS count FROM HasTaken WHERE name = ? GROUP BY GRADE ORDER BY grade ASC";
                PreparedStatement stmt2 = con.prepareStatement(sql2,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stmt2.setString(1,line);
                ResultSet rs2 = stmt2.executeQuery();
                while(rs2.next()) {
                    System.out.println(rs2.getString("grade") + "\t" + rs2.getInt("count"));
                }


            }

            else if(line.equals("7")) {
                System.out.println("Please enter the query.");
                line = sc.nextLine();
                PreparedStatement stmt = con.prepareStatement(line,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                ResultSetMetaData meta = rs.getMetaData();
                int count = meta.getColumnCount();
                for(int i=1;i<=count;i++) {
                    System.out.print(meta.getColumnName(i) + "\t");
                }
                System.out.println("");

                while(rs.next()) {
                    for(int i=1;i<=count;i++) {
                        System.out.print(rs.getString(i) + "\t");
                    }
                    System.out.println("");
                }

            }

            else if(line.equals("8")) {
                System.out.println("Goodbye.");
                break;
            }
            else {
                System.out.println("Input Valid Number");
            }

        }

        sc.close();

        }catch(Exception e){ System.out.println(e);}  
        


    }

    public static double gradeConverter(String grade) {
        switch(grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }
    
    public static void studentPrinter(ResultSet rs,Connection con) {
        try{
            int count = 0;
            while(rs.next()) {
                count++;
            }
            System.out.println(count+ " students found");
            rs.beforeFirst();
            
            while (rs.next()) {
                System.out.println("");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String id = rs.getString("id");
                System.out.println(lastName + ", "+firstName);
                System.out.println("ID: " + id);

                String sqlMajor = "Select dname FROM Majors Where sid = ?";
                PreparedStatement stmtMajor = con.prepareStatement(sqlMajor);
                stmtMajor.setString(1,id);
                ResultSet rsMajor = stmtMajor.executeQuery();
                String major = "";
                System.out.print("Major(s): ");
                while(rsMajor.next()) {
                    major = rsMajor.getString("dname");
                    System.out.print(major +" ");

                }
                System.out.println("");

                String sqlMinor = "Select dname FROM Minors Where sid = ?";
                PreparedStatement stmtMinor = con.prepareStatement(sqlMinor);
                stmtMinor.setString(1,id);
                ResultSet rsMinor = stmtMinor.executeQuery();
                String minor = "";
                System.out.print("Minor(s): ");
                if(rsMinor.next()) {
                    minor = rsMinor.getString("dname");
                    System.out.print(minor + " ");
                }
                System.out.println("");


                String sql2 = "Select * FROM HasTaken WHERE sid = ?";
                PreparedStatement stmt2 = con.prepareStatement(sql2);
                stmt2.setString(1,id);
                ResultSet rs2 = stmt2.executeQuery();
                double gradeNum = 0.0;
                int credits = 0;
                double GPA = 0.0;
                int realCredit = 0;
                while(rs2.next()) {
                    String grades = rs2.getString("grade");
                    String cName = rs2.getString("name");
                    String sql3 = "Select credits FROM Classes WHERE name = ?";
                    PreparedStatement stmt3 = con.prepareStatement(sql3);
                    stmt3.setString(1, cName);
                    ResultSet rs3 = stmt3.executeQuery();
                    if(rs3.next()) {
                        int credit = rs3.getInt("credits");
                        credits = credits + credit;
                        if(grades.equals("F") == false) {
                            realCredit = realCredit + credit;
                        }    
                        gradeNum = gradeNum + (credit * gradeConverter(grades));
                    }
                    
                }
                GPA = gradeNum / credits;
                
                System.out.println("GPA: "+ String.format("%.3f",GPA));
                System.out.println("Credits: " + realCredit);

            }

        }catch(Exception e){ System.out.println(e);}  
    }


}