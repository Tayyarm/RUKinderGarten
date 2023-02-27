package kindergarten;

//import javax.lang.model.util.ElementScanner14;

//import org.w3c.dom.Node;
//import org.xml.sax.EntityResolver;

/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {
        StdIn.setFile(filename);
        int x = StdIn.readInt();
        for(int i =0; i<x; i++)
        {
            
           String FirstName = StdIn.readString();
           String LastName = StdIn.readString();
           int Height = StdIn.readInt();
           Student S = new Student(FirstName, LastName, Height);
           SNode n = new SNode(S, null);
           if(studentsInLine==null)
           {
            studentsInLine = n;
           }
           else 
           {        
            SNode ptr = studentsInLine;
             while (ptr!=null)
            {  
               if(ptr.getStudent().compareNameTo(S)>0 || ptr.getStudent().compareNameTo(S)==0) //head
               {
                 n.setNext(ptr);
                 studentsInLine=n;
                 break;
               }     
                
             else if (ptr.getStudent().compareNameTo(S)<0 ||ptr.getStudent().compareNameTo(S)==0)
             {  
                if(ptr.getNext()==null) // tail
                {
                  ptr.setNext(n);
                  break;   
                }
                if(ptr.getNext().getStudent().compareNameTo(S)>0|| ptr.getStudent().compareNameTo(S)==0) // mid
                    {
                        SNode prev = ptr;
                        ptr=ptr.getNext();
                        prev.setNext(n);
                        n.setNext(ptr);  
                        break;                
                    }                 
             }    
             ptr=ptr.getNext();
           }
           
        }
    }
        
        

        // WRITE YOUR CODE HERE
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        seatingAvailability=new boolean[r][c];
        studentsSitting=new Student[r][c];
        for(r=0; r<seatingAvailability.length; r++)
          {
            for(c=0; c<seatingAvailability[r].length; c++)
            {
                boolean x=StdIn.readBoolean();
                seatingAvailability[r][c]= x;
            }
          }
	// WRITE YOUR CODE HERE
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () 
    {

       /*System.out.println("seatStudents: 1---------------------------------------");     
       printMusicalChairs();
       printStudentsInLine();
       printSeatedStudents();  
       System.out.println("seatStudents: 2---------------------------------------");*/
       if(studentsInLine==null&&musicalChairs!=null)
       {
         studentsInLine=musicalChairs;
         musicalChairs.setNext(null);
         musicalChairs=null;
       }

        boolean isStudent=false;
       while(studentsInLine!=null)
       {
        Student s = null;
        if(musicalChairs!=null)
        {
           s=musicalChairs.getStudent();
           musicalChairs=null;
        }
         else
         {
            s=studentsInLine.getStudent();
            isStudent=true;
         }
         boolean add=false;        
         for(int r =0; r<seatingAvailability.length;r++)
        {
            for(int c = 0; c<seatingAvailability[r].length;c++)
            {
               if(seatingAvailability[r][c]==true && studentsSitting[r][c]==null)
               { 
                    studentsSitting[r][c] = s;
                    add=true;
                    break;
               }                 
            }
            if(add==true)
                break;
          }
          if(isStudent==true)
            studentsInLine= studentsInLine.getNext();
       }
 
	// WRITE YOUR CODE HERE
	
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        
        for(int r=0; r<studentsSitting.length; r++)
        {
            for(int c=0; c<studentsSitting[r].length; c++)
            {
                if(studentsSitting[r][c]==null)
                    continue;
               Student S = studentsSitting[r][c];
               studentsSitting[r][c]=null;
               SNode n = new SNode(S, null);
               if(musicalChairs==null)
               {
                 musicalChairs=n;
                 musicalChairs.setNext(musicalChairs);
               }
               else
               {
                  SNode front = musicalChairs.getNext();
                  musicalChairs.setNext(n);
                  musicalChairs = n;
                  musicalChairs.setNext(front);
               }
               
            }
        }
        
        // WRITE YOUR CODE HERE

     }
     private void insertStudentsByHeight(SNode P)
     {
        //System.out.print( P.getStudent().print()+" | ");
       
        if(studentsInLine==null)
        {
            studentsInLine=P;
        }
         else
         {
           SNode prev = null;
           SNode ptr =studentsInLine;
           while(ptr!=null)
           {  
                if(P.getStudent().getHeight()<=ptr.getStudent().getHeight())
                {
                   break;
                }
                else
                {
                    prev=ptr;
                    ptr=ptr.getNext();
                }
           }
           if(prev==null&&ptr!=null)
           {
             studentsInLine=P;
             studentsInLine.setNext(ptr);
           }
           else if(prev!=null && ptr!=null)
           {
             prev.setNext(P);
             P.setNext(ptr);
           }
           else
           {
             prev.setNext(P);
           }
      }
    }         

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        int count = 0;
        SNode ptr = musicalChairs;
        do
        {
            count++;
            ptr = ptr.getNext();

        } while(ptr!=musicalChairs);
       // System.out.println("playMusicalChairs count===================="+count);
        int CurrCount = count; 
        for(int i =0; i<count-1; i++)
        {
            int x = StdRandom.uniform(CurrCount);
            int temp =0;
            SNode p = musicalChairs;
            SNode n = musicalChairs.getNext();
            while(temp<=x)
            {
             if(x==temp)
             {
                if(p.getNext()==musicalChairs)
                {
                    musicalChairs=p;
                }
                p.setNext(n.getNext());
                n.setNext(null);
               
                CurrCount--;
               insertStudentsByHeight(n);
               break;
             }
             else
                {
                    p=p.getNext();
                    n=n.getNext();
                    temp ++;
                }
            }
            
        }
         seatStudents();
    } 

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) 
    {
        
        // WRITE YOUR CODE HERE
        Student s = new Student(firstName, lastName, height);
        if(studentsInLine!=null)
        {
            SNode n= new SNode(s, null);
            SNode ptr = studentsInLine;
            while(ptr!=null)
            {
               if(ptr.getNext()==null)
               {
                 ptr.setNext(n);
                 break;
               }
                 ptr=ptr.getNext();
            }        
            
        }
        else if(musicalChairs!=null)
        {
            SNode n = new SNode(s, null);
            SNode ptr = musicalChairs;
            SNode front = musicalChairs.getNext();
            
                if(ptr.getNext()==front)
                {
                    musicalChairs.setNext(n);
                    musicalChairs=n;
                    musicalChairs.setNext(front);
                }
        }        
        else
            {
                boolean add=false;
                for(int r=0; r<seatingAvailability.length; r++)
                {
                   for(int c=0; c<seatingAvailability[r].length; c++)
                   {
                      if(seatingAvailability[r][c]==true && studentsSitting[r][c]==null)
                      {
                         studentsSitting[r][c]= s;
                         add=true;
                         break;
                      }
                   }
                   if(add==true)
                   {
                     break;
                   }
                }
            }

    }
        
    

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        // WRITE YOUR CODE HERE
        Student s = new Student(firstName, lastName, 0);
        if(studentsInLine!=null)
        {
            SNode prev=null;
            SNode ptr=studentsInLine;
            while(ptr!=null)
            {
                if(s.compareNameTo(ptr.getStudent())==0 && prev==null&&ptr!=null)
                {
                   studentsInLine=ptr.getNext();
                   ptr.setNext(null);
                   break;
                }
                else if(s.compareNameTo(ptr.getStudent())==0 && prev!=null && ptr!=null )
                {
                   prev.setNext(ptr.getNext());
                   ptr.setNext(null);
                   break;
                }
                else 
                {
                    prev=ptr;
                    ptr=ptr.getNext();
                }
            }
        }
        else if(musicalChairs!=null)
        {
            
            if(musicalChairs==musicalChairs.getNext())
            {
                if(s.compareNameTo(musicalChairs.getStudent())==0)
                {
                  musicalChairs=null;  
                }
                return;
            }
         /*    SNode prev=musicalChairs;
            SNode ptr=musicalChairs.getNext();
            do
            {
                if(s.compareNameTo(ptr.getStudent())==0 && prev==musicalChairs && ptr==musicalChairs.getNext() || //head
                   s.compareNameTo(ptr.getStudent())==0 && prev!=musicalChairs && ptr!=musicalChairs.getNext()) //mid
                {
                   prev.setNext(ptr.getNext());
               //    ptr.setNext(null);
                   break;      
                }
                else if(s.compareNameTo(ptr.getStudent())==0 && ptr==musicalChairs)
                {
                   musicalChairs=prev;
                   musicalChairs.setNext(ptr.getNext());
                 //  ptr.setNext(null);
                   break;  
                }
                else
                {
                    prev=ptr;
                    ptr=ptr.getNext();
                }
                
            }while(ptr!=musicalChairs.getNext());*/
            SNode f = musicalChairs.getNext();
        SNode c= f;
        SNode p =musicalChairs;
        do
        {
            if(s.compareNameTo(c.getStudent())==0)
            {
              if(c==f)
              {
                p.setNext(c.getNext());
              }
              else if(c==musicalChairs)
              {
                 p.setNext(c.getNext());
                 musicalChairs=p;
              }
              else
              {
                p.setNext(c.getNext());
              }
                break;
            }
             p=c;
             c=c.getNext();  
        }while(c!=f);
        }

        else
        {
            boolean remove=false;
            for(int r=0; r<seatingAvailability.length;r++)
            {
                for(int c=0; c<seatingAvailability[r].length; c++)
                {
                    if(seatingAvailability[r][c]==true && s.compareNameTo(studentsSitting[r][c])==0)
                    {
                        studentsSitting[r][c]=null;
                        remove=true;
                        break;
                    }
                }
                if(remove==true)
                {
                    break;
                }
            }

        }
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
