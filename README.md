<h1>Setting up the environment</h1>
This project uses Java SDK 17 and JavaFX 17.0.2. The group also agreed to use NetBeans 12.6 as a common IDE. These are the instructions for how to set up JavaFX and run the project in NetBeans. A video guide on how to perform this setup can be found here: https://www.youtube.com/watch?v=uU_Ajtz0Qpc.

<h2>Install Java, JavaFX and NetBeans</h2>

- Download and install JDK 17: https://www.oracle.com/java/technologies/downloads/
- Download and install NetBeans 12.6: https://netbeans.apache.org/download/nb126/nb126.html
- Download JavaFX 17.0.2 and unzip to your desired location: https://gluonhq.com/products/javafx/

<h2>Configure JavaFX in NetBeans</h2>

If this is your first time running NetBeans, you might have to install basic Java support:

- In NetBeans go to Tools -> Options -> Java -> Java Shell. Somewhere along the way NetBeans will ask you to install Java support.
- In Java Shell options you can select your platform. If JDK 17 is not available, select Manage -> Add Platform -> Java Standard Edition and browse for the location where you installed the JDK.

Add JavaFX as a library in NetBeans:

- Go to Tools -> Libraries.
- Select New Library.
- Give the library a name (for example JavaFX 17) and keep library type as Class Libraries
- With your new library selected, clika Add JAR/Folder
- Navigate to the location where you unzipped JavaFX and the lib-subfolder <path>javafx-sdk-17.02\lib\
- Select ALL .jar files in the lib folder and click Add/JAR Folder

<h2>Clone and set up the project</h2>

Clone repo:

- In NetBeans go to Team -> Git -> Clone
- At the Repository page, specify the path to the Git repository location, user name and password (you can save them for the future). Click Next.
- At the Remote Branches page, select the repository branch(es) to be fetched to your local repository. Click Next.
- At the Destination Directory page, specify the directory where you want it stored locally and the branch you want to check out.

Create project in IDE:

- Once clone is completed, you will be asked if you want to create an IDE project. Click on Create Project.
- At the Choose Project page, choose **Java with Ant** and **Java Application**. **IMPORTANT: Do NOT select JavaFX, even if it shows up as an option.**
- At the Name and Location page, set Project Name to **SoftwareDesignProject** and untick Create Main Class. Click Finish.

Configure JavaFX in the project:

- Once the project is created, right click the project in the projects tab on the left and select Properties.
- In Categories on the left, navigate to Libraries.
- In Libraries -> Compile, click the + sign next to **Classpath**, select Add Library, locate the JavaFX library you created earlier and click Add Library.
- Still in Libraries category, switch to Run tab, Click the + sign next to **Modulepath** select Add Libraries and add your JavaFX library as you did in the previous step.
- On the left, navigate to Run category and copy/paste the following text into the VM Options text area: --module-path "<YOUR_PATH>\javafx-sdk-17.0.2\lib" --add-modules "javafx.controls,javafx.fxml"  
- Replace <YOUR_PATH> with the full path to the folder where you installed javafx.

Now you should be ready to run the project in NetBeans. To try it out with the prototype:

- If not already on the prototype branch, goto Team -> Branch/Tag -> Switch to branch.
- Select the prototype branch.
- Locate Prototype.java under Source Packages -> default package, right click and select Run File.

<h2>Useful links</h2>

- Using Git in NetBeans: https://netbeans.apache.org/kb/docs/ide/git.html
