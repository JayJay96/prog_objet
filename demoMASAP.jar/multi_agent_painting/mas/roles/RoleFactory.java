package multi_agent_painting.mas.roles;

import multi_agent_painting.mas.exceptions.RoleInitException;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Epulapp on 12/01/2017.
 */
public class RoleFactory {
    private static final RoleFactory INSTANCE = new RoleFactory();

    private RoleFactory(){};

    public static RoleFactory getInstance(){
        return INSTANCE;
    }

    public AbstractRole createRole(Class className) throws RoleInitException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //Récupérer le constructeur de la classe et retourner une nouvelle instance. Ne sont pas afficher dans le diagramme uml
        return (AbstractRole) className.getConstructor().newInstance();
    }

    public AbstractRole createRoleInit(Class className) throws RoleInitException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (AbstractRole) className.getConstructor(Boolean.class).newInstance();
    }
}
