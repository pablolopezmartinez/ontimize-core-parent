package com.ontimize.gui.econ;

public class BeneficiaryDataTransfer {

	protected String beneficiaryCode = null;
	protected double amount = 0.0;
	protected String bankNumber = null;
	protected String branchNumber = null;
	protected String currentAccount = null;
	protected char costs = ' ';
	protected char concept = ' ';
	protected String controlDigit = null;
	protected String beneficiaryName = null;
	protected String beneficiaryAddress = null;
	protected String beneficiaryZipCode = null;
	protected String beneficiaryProvince = null;
	protected String transferConcept = null;
	protected String dni = null;

	public BeneficiaryDataTransfer(String benfCode, double amount, String bankNumber, String branchNumber, String accountNumber, char costs, char concept, String controlDigit,
			String beneficiaryName, String benefAddress, String zipCode, String province, String transferConcept, String dni) {
		this.beneficiaryCode = benfCode;
		this.dni = dni;
		this.amount = amount;
		this.bankNumber = bankNumber;
		this.branchNumber = branchNumber;
		this.currentAccount = accountNumber;
		this.costs = costs;
		this.concept = concept;
		this.controlDigit = controlDigit;
		this.beneficiaryName = beneficiaryName;
		this.beneficiaryAddress = benefAddress;
		this.beneficiaryZipCode = zipCode;
		this.beneficiaryProvince = province;
		this.transferConcept = transferConcept;
	}

	public String getBeneficiaryCode() {
		return this.beneficiaryCode;
	}

	public String getDNI() {
		return this.dni;
	}

	public double getAmount() {
		return this.amount;
	}

	public String getBankNumber() {
		return this.bankNumber;
	}

	public String getBranch() {
		return this.branchNumber;
	}

	public String getCurrentAccount() {
		return this.currentAccount;
	}

	public char getCosts() {
		return this.costs;
	}

	public char getConcept() {
		return this.concept;
	}

	public String getControlDigit() {
		return this.controlDigit;
	}

	public String getBeneficiaryName() {
		return this.beneficiaryName;
	}

	public String getBeneficiaryZipCode() {
		return this.beneficiaryZipCode;
	}

	public String getBeneficiaryProvince() {
		return this.beneficiaryProvince;
	}

	public String getTransferConcept() {
		return this.transferConcept;
	}

	public String getBeneficiaryAddress() {
		return this.beneficiaryAddress;
	}

	@Override
	public String toString() {
		return this.getBeneficiaryCode() + "," + this.getConcept() + "," + this.getTransferConcept() + "," + this.getBeneficiaryZipCode() + "," + this
				.getControlDigit() + "," + this.getDNI() + "," + this.getBeneficiaryAddress() + "," + this.getCosts() + "," + this.getAmount() + "," + this
						.getBeneficiaryName() + "," + this.getCurrentAccount() + "," + this.getBankNumber() + "," + this.getBranch() + "," + this.getBeneficiaryProvince();
	}
};