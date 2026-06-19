package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Scannerrrs {
    public static ArrayList<String> ScannerController(String ChemineSource, String packageController){
        ArrayList<String> ListController = new ArrayList<>();
        String a = packageController.replace("/", ".");
        String chemine = ChemineSource;
        String chemineWPack = ChemineSource + "/" + a;
        Path cheminFichier = Paths.get(chemineWPack);
        //Path cheminFichier = Paths.get("/opt/tomcat/webapps/testFramework/WEB-INF/classes");
        try {
            Stream<Path> chemins = Files.walk(cheminFichier);
            chemins.forEach(chemin -> {
                if (!Files.isDirectory(chemin)){
                    String cheminString = chemin.toString().replace(chemin.getFileSystem().getSeparator(), ".");
                    String cheminVrais = cheminString.substring(chemine.length() + 1);
                    String CheminVraissansPointClasse = cheminVrais.substring(0, cheminVrais.length()-6);
                    Class<?> kilasy = null;
                    try {
                    kilasy = Class.forName(CheminVraissansPointClasse);
                    } catch (Exception e) {
                        
                    }
                    if(kilasy != null && kilasy.isAnnotationPresent(mg.itu.Controller.class)){
                        ListController.add(kilasy.getName());
                        //this.Controller.add(cheminString);
                    }
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ListController;
    }
}
