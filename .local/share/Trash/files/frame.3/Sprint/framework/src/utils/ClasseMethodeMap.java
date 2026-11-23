package utils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.reflect.Field;
import mg.itu.Param;

public class ClasseMethodeMap {
    Class<?> kilasy;
    String nomMethode;
    Map<String, Class<?>[]> nomEtTyPeArg = new LinkedHashMap<>();
    boolean Staticite;
    boolean WebRest;
    boolean AuthCOnnect;
    ArrayList<String> Role;
    String packModel = "";

    public String getPackModel() {
        return packModel;
    }
    public void setPackModel(String packModel) {
        this.packModel = packModel == null ? "" : packModel.trim();
    }
    // true si la classe est dans le package de model défini par PackModel (web.xml)
    public static boolean estDansPackModel(Class<?> type, String packModel) {
        if (type == null || packModel == null || packModel.trim().isEmpty()) return false;
        String pack = packModel.trim();
        if (!pack.endsWith(".")) pack += ".";
        return type.getName().startsWith(pack);
    }


    public ArrayList<String> getRole() {
        return Role;
    }
    public void setRole(ArrayList<String> role) {
        Role = role;
    }
    public Class<?> getKilasy() {
        return kilasy;
    }
    public void setKilasy(Class<?> kilasy) {
        this.kilasy = kilasy;
    }
    public String getNomMethode() {
        return nomMethode;
    }
    public void setNomMethode(String nomMethode) {
        this.nomMethode = nomMethode;
    }
    public ClasseMethodeMap() {
    }
    
    public ClasseMethodeMap(Class<?> kilasy, String nomMethode, Map<String, Class<?>[]> nomEtTyPeArg) {
        this.kilasy = kilasy;
        this.nomMethode = nomMethode;
        this.nomEtTyPeArg = nomEtTyPeArg;
    }
    public void CompletMethods()throws Exception{
        Method methode = null;
        for (Method m : kilasy.getMethods()) {
            if (m.getName().equals(this.nomMethode)) {
                methode = m;
            }
        }
        // les type de arguments
        Parameter[] parameters = methode.getParameters();
        for (Parameter p : parameters) {
            Param annotation = p.getAnnotation(Param.class);
            String nomArgument = annotation != null ? annotation.value() : p.getName();
            Class<?> typeArgument = p.getType();       
            if (estDansPackModel(typeArgument, this.packModel)){
                // Field[] fields = typeArgument.getDeclaredFields();
                // for (Field field : fields) {
                //     String nomAttribut = field.getName()+"_"+nomArgument;
                //     Class<?> typeAttribut = field.getType();
                //     Class<?>[] args = new Class[2];
                //     args[0] = typeAttribut;
                //     args[1] = typeArgument;
                //     this.nomEtTyPeArg.put(nomAttribut, args);
                // }
                for (Map.Entry<String, Accesseur> e : RegistreAccesseurs.pour(typeArgument).entrySet()) {
                    String nomAttribut = e.getKey() + "_" + nomArgument;
                    Class<?>[] args = new Class[2];
                    args[0] = e.getValue().getType();
                    args[1] = typeArgument;
                    this.nomEtTyPeArg.put(nomAttribut, args);
                }
            }else {
                Class<?>[] args = new Class[2];
                args[0] = typeArgument;
                this.nomEtTyPeArg.put(nomArgument, args);
            }
        }
        // static ou pas
            int modificateurs = methode.getModifiers();

            if (Modifier.isStatic(modificateurs)) {
                System.out.println("La méthode est STATIC !");
                this.Staticite = true;
            } else {
                System.out.println("La méthode n'est PAS static (méthode d'instance).");
                this.Staticite = false;
            }
    }
    public Map<String, Class<?>[]> getNomEtTyPeArg() {
        return nomEtTyPeArg;
    }
    public void setNomEtTyPeArg(Map<String, Class<?>[]> nomEtTyPeArg) {
        this.nomEtTyPeArg = nomEtTyPeArg;
    }
    public boolean isStaticite() {
        return Staticite;
    }
    public void setStaticite(boolean staticite) {
        Staticite = staticite;
    }
    public boolean isWebRest() {
        return WebRest;
    }
    public void setWebRest(boolean webRest) {
        WebRest = webRest;
    }
    public void setAuthCOnnect(boolean AuthCOnnecte){
        this.AuthCOnnect = AuthCOnnecte;
    }
    public boolean isAuthCOnnect(){
        return this.AuthCOnnect;
    }
}