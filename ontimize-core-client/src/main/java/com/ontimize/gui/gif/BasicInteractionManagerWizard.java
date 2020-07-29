package com.ontimize.gui.gif;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ontimize.gui.Form;
import com.ontimize.gui.InteractionManager;
import com.ontimize.gui.button.Button;
import com.ontimize.gui.container.CardPanel;
import com.ontimize.gui.manager.IFormManager;

/**
 * Implementation of a wizard interaction manager.<br>
 * This class uses the CardPanel component in the form
 *
 * @version 1.0
 * @see inicializa
 */
public abstract class BasicInteractionManagerWizard extends InteractionManager {

    public static final String FINISH = "finish";

    public static final String CANCEL = "cancel";

    public static final String NEXT = "next";

    public static final String PREVIOUS = "previous";

    protected int step = 1;

    public BasicInteractionManagerWizard() {
    }

    protected abstract String getAttrCardPanel();

    public abstract int getStepsNumber();

    protected abstract String getComponentIdToShowInStep(int step);

    protected abstract void end();

    protected abstract boolean validateFinish();

    protected abstract boolean validStepChange(int newStep, int previousStep);

    protected abstract void cancel();

    public int getStep() {
        return this.step;
    }

    @Override
    public void setInitialState() {
        super.setInitialState();
        this.start();
    }

    protected void start() {
        this.setCurrentStep(1);
    }

    public int getCurrentStep() {
        return this.step;
    }

    public boolean setCurrentStep(int step) {
        int previousStep = this.step;
        if (this.validStepChange(step, previousStep)) {
            this.step = step;
            this.updateButtonsState();
            this.changedStep(this.step, previousStep);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Using {@link #setCurrentStep}
     * @deprecated
     * @param step
     * @return
     */

    @Deprecated
    public boolean setActualStep(int step) {
        return this.setCurrentStep(step);
    }

    protected void changedStep(int newStep, int previousStep) {
        String id = this.getComponentIdToShowInStep(newStep);
        if (id != null) {
            CardPanel cp = (CardPanel) this.managedForm.getElementReference(this.getAttrCardPanel());
            cp.show(id);
        }
    }

    protected void updateButtonsState() {
        this.managedForm.getButton(BasicInteractionManagerWizard.CANCEL).setEnabled(true);
        this.managedForm.getButton(BasicInteractionManagerWizard.PREVIOUS).setEnabled(this.step != 1);
        this.managedForm.getButton(BasicInteractionManagerWizard.NEXT).setEnabled(this.step < this.getStepsNumber());
        this.managedForm.getButton(BasicInteractionManagerWizard.FINISH).setEnabled(this.step == this.getStepsNumber());
    }

    protected void next() {
        if (this.step >= this.getStepsNumber()) {
            throw new IllegalStateException("Error: No more transitions available. Last step reached");
        }
        this.setCurrentStep(this.step + 1);
    }

    protected void previous() {
        if (this.step <= 1) {
            throw new IllegalStateException("Error: No more transitions available. First step reached");
        }
        this.setCurrentStep(this.step - 1);
    }

    @Override
    public void registerInteractionManager(Form form, IFormManager formManager) {
        super.registerInteractionManager(form, formManager);
        // Next button
        Button bNext = this.managedForm.getButton(BasicInteractionManagerWizard.NEXT);
        if (bNext != null) {
            bNext.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    BasicInteractionManagerWizard.this.next();
                }
            });
        }

        // Go back button
        Button bPrevious = this.managedForm.getButton(BasicInteractionManagerWizard.PREVIOUS);
        if (bPrevious != null) {
            bPrevious.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    BasicInteractionManagerWizard.this.previous();
                }
            });
        }

        // Finish button
        Button bFinish = this.managedForm.getButton(BasicInteractionManagerWizard.FINISH);
        if (bFinish != null) {
            bFinish.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (BasicInteractionManagerWizard.this.validateFinish()) {
                        BasicInteractionManagerWizard.this.end();
                    }
                }
            });
        }

        // Cancel button
        Button bCancel = this.managedForm.getButton(BasicInteractionManagerWizard.CANCEL);
        if (bCancel != null) {
            bCancel.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    BasicInteractionManagerWizard.this.cancel();
                }
            });
        }
    }

}
