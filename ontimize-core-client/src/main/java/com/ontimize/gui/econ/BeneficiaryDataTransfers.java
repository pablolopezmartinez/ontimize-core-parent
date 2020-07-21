package com.ontimize.gui.econ;

import java.util.Vector;

public class BeneficiaryDataTransfers {

    protected Vector data = new Vector();

    public BeneficiaryDataTransfers() {
    }

    public void add(String code, double amount, String bankNumber, String branchNumber, String accountNumber, char cost,
            char concept, String controlDigit, String name,
            String address, String zipCode, String province, String transferConcep, String dni) {

        this.data.add(new BeneficiaryDataTransfer(code, amount, bankNumber, branchNumber, accountNumber, cost, concept,
                controlDigit, name, address, zipCode, province,
                transferConcep, dni));
    }

    public int getSize() {
        return this.data.size();
    }

    public BeneficiaryDataTransfer getTransferData(int index) {
        if ((index < 0) || (index >= this.getSize())) {
            return null;
        }
        return (BeneficiaryDataTransfer) this.data.get(index);
    }

}
