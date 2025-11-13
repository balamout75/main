import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;

class CustomDialog extends JDialog {
    private String typedText = null;

    private String magicWord;
    private JOptionPane optionPane;

    public String getValidatedText() {
        return typedText;
    }

    //public CustomDialog(Frame aFrame, String aWord, SocioInput parent) {
    public CustomDialog(Frame aFrame, String aName, String aWord) {
        super(aFrame, true);
        //final SocioInput dd = parent;

        magicWord = aWord.toUpperCase();
        setTitle("¬ведите переменную");

        //final String msgString1 = "¬ведите ";
        //final String msgString2 = "(The answer is \"" + magicWord + "\".)";
        final JTextField textField = new JTextField(10);
        Object[] array = {aName, aWord, textField};

        final String btnString1 = "Enter";
        final String btnString2 = "Cancel";
        Object[] options = {btnString1, btnString2};

        optionPane = new JOptionPane(array, 
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(btnString1);
            }
        });

        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                if (isVisible() 
                 && (e.getSource() == optionPane)
                 && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
                     prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                    Object value = optionPane.getValue();

                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    // Reset the JOptionPane's value.
                    // If you don't do this, then if the user
                    // presses the same button next time, no
                    // property change event will be fired.
                    optionPane.setValue(
                            JOptionPane.UNINITIALIZED_VALUE);

                    //if (value.equals(btnString1)) {
                    if (true) {
                            typedText = textField.getText();
                        String ucText = typedText.toUpperCase();
                        //if (ucText.equals(magicWord)) {
                        if (true) {
                            // we're done; dismiss the dialog
                            setVisible(false);
                        } else { 
                            // text was invalid
                            textField.selectAll();
                            JOptionPane.showMessageDialog(
                                            CustomDialog.this,
                                            "Sorry, \"" + typedText + "\" "
                                            + "isn't a valid response.\n"
                                            + "Please enter " 
                                            + magicWord + ".",
                                            "Try again",
                                            JOptionPane.ERROR_MESSAGE);
                            typedText = null;
                        }
                    } else { // user closed dialog or clicked cancel
                        //dd.setLabel("It's OK.  " + "We won't force you to type "  + magicWord + ".");
                        typedText = null;
                        setVisible(false);
                    }
                }
            }
        });
    }
}
