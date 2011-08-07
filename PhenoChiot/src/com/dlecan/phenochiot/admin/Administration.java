package com.dlecan.phenochiot.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.dlecan.phenochiot.framework.Util;
import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;
import com.dlecan.phenochiot.framework.exception.ExceptionTechnique;

/**
 * 
 * @author dam
 *
 * @revision $Revision: 1.9 $
 * @version $Name:  $
 * @date $Date: 2004/01/07 21:06:38 $
 * @id $Id: Administration.java,v 1.9 2004/01/07 21:06:38 fris Exp $
 */
public class Administration extends ApplicationWindow {

    /**
     * Logger de la classe
     */
    private static Logger log = Logger.getLogger(Administration.class);
    
    /**
     * 
     */
    private PanneauDroit panneauDroit = null;

    /**
     * 
     * @param parentShell
     */
    public Administration(Shell parentShell) {
        super(parentShell);
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        log.debug("Début méthode main");
        Administration application = new Administration(null);
        application.createComponents();
        application.open();
        Display.getCurrent().dispose();
        log.debug("Fin méthode main");
    }

    /**
     * 
     *
     */
    public void createComponents() {
        createShell();
        addMenuBar();
        addStatusLine();
        addToolBar(SWT.FLAT | SWT.WRAP);
        setBlockOnOpen(true);
        setDefaultImage(Util.getImageRegistry().get("logo_petit"));
    }

    /**
     * 
     * @see org.eclipse.jface.window.ApplicationWindow#createMenuManager()
     */
    protected MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager();
        menuManager.add(createMenu());
        return menuManager;
    }

    /**
     * 
     * @return
     */
    private MenuManager createMenu() {
        MenuManager menu = new MenuManager("?", "Id01");
        menu.add(new GroupMarker("Aide"));
        menu.add(createAideAction());
        menu.add(createQuitterAction());
        return menu;
    }

    /**
     * @return
     */
    private IAction createQuitterAction() {
        return new Action() {
            public String getText() {
                return "Quitter";
            }

            public void run() {
                quitter();
            }
        };
    }

    /**
     * 
     * @return
     */
    private Action createAideAction() {
        return new Action() {
            public String getText() {
                return "A propos";
            }

            public void run() {
                String[] tab = {IDialogConstants.OK_LABEL};
                MessageDialog dialog = new MessageDialog(getShell(),
                        "A propos", null, "Contenu à venir",
                        MessageDialog.INFORMATION, tab, 0);
                dialog.open();
            }
        };
    }

    /**
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
    }

    /**
     * 
     * @see org.eclipse.jface.window.Window#getInitialSize()
     */
    protected Point getInitialSize() {
        return getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
    }

    /**
     * 
     * @return
     */
    protected Point getInitialLocation() {
        return new Point(600, 400);
    }

    /**
     * 
     * @see org.eclipse.jface.window.Window#handleShellCloseEvent()
     */
    protected void handleShellCloseEvent() {
        quitter();
    }

    /**
     * 
     *
     */
    private void quitter() {
        panneauDroit.verifierDonnneesAJour();
        
        boolean confirmation = afficherConfirmation(getShell(), "Quitter",
                "Voulez-vous vraiment quitter cette application ?");

        // L'utilisateur veut quitter
        if (confirmation) {
            setReturnCode(CANCEL);
            close();
        }
    }

    /**
     * 
     * @see org.eclipse.jface.window.ApplicationWindow#createToolBarManager(int)
     */
    protected ToolBarManager createToolBarManager(int style) {
        return new ToolBarManager(style);
    }

    /**
     * 
     * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {
        getShell().setText("Phénochiot - Administration");

        getShell().setMaximized(true);

        SashForm sash_form = new SashForm(parent, SWT.HORIZONTAL | SWT.NONE);

        final Tree tree = new Tree(sash_form, SWT.BORDER);

        panneauDroit = new PanneauDroit(sash_form, SWT.NONE);

        // 30%/70%, taille respective des 2 panneaux
        sash_form.setWeights(new int[]{15, 85});

        // On clique sur une race, maj du panneau droit
        tree.addListener(SWT.Selection, new Listener() {
            public void handleEvent(final Event event) {
                log.debug("Début méthode Selection.handleEvent");

                final TreeItem item = (TreeItem) event.item;

                log.debug("Donnée de l'item : " + item.getData());

                final Shell splash = new Shell(getShell(), SWT.BORDER
                        | SWT.APPLICATION_MODAL);
                final ProgressIndicator bar = new ProgressIndicator(splash);
                splash.setLayout(new FillLayout(SWT.HORIZONTAL));
                bar.beginTask(20);
                splash.pack();
                Rectangle splashRect = splash.getBounds();
                Rectangle displayRect = Display.getCurrent().getBounds();
                int x = (displayRect.width - splashRect.width) / 2;
                int y = (displayRect.height - splashRect.height) / 2;
                splash.setLocation(x, y);
                splash.open();

                Display.getCurrent().syncExec(new Runnable() {
                    public void run() {

                        bar.worked(10.0);

                        panneauDroit.majDonnees(((Integer) item.getData())
                                .intValue());

                        bar.worked(10.0);

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // Rien
                        }

                        bar.sendRemainingWork();
                        bar.done();
                        splash.close();
                        bar.dispose();
                        splash.dispose();
                    }
                });
                log.debug("Fin méthode Selection.handleEvent");
            }
        });

        // On double-clic sur une race, édition du nom de cette race
        tree.addListener(SWT.MouseDoubleClick, new Listener() {
            public void handleEvent(final Event event) {
                //                log.debug("Début méthode MouseDoubleClick.handleEvent");
                event.toString();
                // TODO : gérer l'édition du nom de la race
                //                log.debug("Fin méthode MouseDoubleClick.handleEvent");
            }
        });

        GestionnaireAccesDonnees gad = null;
        ResultSet resultat = null;

        try {
            gad = GestionnaireAccesDonnees
                    .getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);
            resultat = gad.executerSelect(1);

            while (resultat.next()) {
                TreeItem root = new TreeItem(tree, 0);
                root.setText(resultat.getString("nom_race"));
                root.setData(new Integer(resultat.getInt("id_race")));
            }

            panneauDroit.creerContenu();

        } catch (ExceptionTechnique e) {
            log.error(e.getMessage(), e);
            afficherErreur(getShell(), e);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            afficherErreur(getShell(), e);
        } finally {
            if (gad != null) {
                gad.terminerRequete(resultat);
            }
        }

        //		addKeyListenerRecursif(panneauDroit, getShell());

        return sash_form;
    }

    /**
     * @param shell
     * @param e
     */
    public static void afficherErreur(Shell shell, Exception e) {
        log.debug("Début méthode afficherErreur");
        MessageDialog.openError(shell, "Erreur", e.getMessage()
                + "\n(Consulter les logs d'erreurs pour plus d'informations)");

        log.debug("Fin méthode afficherErreur");
    }

    /**
     * @param shell
     * @param message
     */
    public static void afficherMessage(Shell shell, String message) {
        log.debug("Début méthode afficherMessage");
        MessageDialog.openInformation(shell, "Information", message);
        log.debug("Fin méthode afficherMessage");
    }

    /**
     * 
     * @param shell
     * @param question
     * @return <code>true</code> si l'utilisateur a cliqué "oui"
     */
    public static boolean afficherQuestion(Shell shell, String question) {
        log.debug("Méthode afficherQuestion");
        return MessageDialog.openQuestion(shell, "Question", question);
    }

    /**
     * 
     * @param shell
     * @param message
     * @param question
     * @return <code>true</code> si l'utilisateur a cliqué "oui"
     */
    public static boolean afficherConfirmation(Shell shell, String message,
            String question) {
        log.debug("Méthode afficherConfirmation");
        return MessageDialog.openConfirm(shell, message, question);
    }

    /**
     * 
     * @param panneau
     * @param controle
     */
    //    private void addKeyListenerRecursif(PanneauDroit panneau, Control controle) {
    //        log.debug("Début méthode addKeyListenerRecursif");
    //
    //        controle.addKeyListener(panneau);
    //        if (Composite.class.isInstance(controle)
    //                && !StyledText.class.isInstance(controle)) {
    //            Control[] cs = ((Composite) controle).getChildren();
    //            for (int i = 0; i < cs.length; i++) {
    //                addKeyListenerRecursif(panneau, cs[i]);
    //            }
    //        }
    //        log.debug("Fin méthode addKeyListenerRecursif");
    //    }
}