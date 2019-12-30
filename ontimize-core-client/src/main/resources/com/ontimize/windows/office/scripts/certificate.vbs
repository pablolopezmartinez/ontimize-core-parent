Option Explicit
                                                               

Const CAPICOM_CURRENT_USER_STORE                               = 2
                                                        
Const CAPICOM_STORE_OPEN_READ_ONLY                             = 0
Const CAPICOM_STORE_OPEN_READ_WRITE                            = 1
Const CAPICOM_STORE_OPEN_MAXIMUM_ALLOWED                       = 2
Const CAPICOM_STORE_OPEN_EXISTING_ONLY                         = 128
Const CAPICOM_STORE_OPEN_INCLUDE_ARCHIVED                      = 256
  
                                                                 
Const CAPICOM_CERTIFICATE_FIND_SHA1_HASH                       = 0
Const CAPICOM_CERTIFICATE_FIND_SUBJECT_NAME                    = 1
Const CAPICOM_CERTIFICATE_FIND_ISSUER_NAME                     = 2
Const CAPICOM_CERTIFICATE_FIND_ROOT_NAME                       = 3
Const CAPICOM_CERTIFICATE_FIND_TEMPLATE_NAME                   = 4
Const CAPICOM_CERTIFICATE_FIND_EXTENSION                       = 5
Const CAPICOM_CERTIFICATE_FIND_EXTENDED_PROPERTY               = 6
Const CAPICOM_CERTIFICATE_FIND_APPLICATION_POLICY              = 7
Const CAPICOM_CERTIFICATE_FIND_CERTIFICATE_POLICY              = 8
Const CAPICOM_CERTIFICATE_FIND_TIME_VALID                      = 9
Const CAPICOM_CERTIFICATE_FIND_TIME_NOT_YET_VALID              = 10
Const CAPICOM_CERTIFICATE_FIND_TIME_EXPIRED                    = 11
Const CAPICOM_CERTIFICATE_FIND_KEY_USAGE                       = 12
                                                             
Const CAPICOM_STORE_SAVE_AS_SERIALIZED                         = 0
Const CAPICOM_STORE_SAVE_AS_PKCS7                              = 1
Const CAPICOM_STORE_SAVE_AS_PFX                                = 2

Const CAPICOM_EXPORT_DEFAULT                                   = 0
Const CAPICOM_EXPORT_IGNORE_PRIVATE_KEY_NOT_EXPORTABLE_ERROR   = 1


' Command line arguments.
Dim OpenMode          : OpenMode          = CAPICOM_STORE_OPEN_MAXIMUM_ALLOWED OR CAPICOM_STORE_OPEN_EXISTING_ONLY 
Dim StoreLocation     : StoreLocation     = CAPICOM_CURRENT_USER_STORE
Dim StoreName         : StoreName         = "My"
Dim Password          : Password          = ""
Dim SaveAs            : SaveAs            = CAPICOM_STORE_SAVE_AS_SERIALIZED

On Error Resume next

Dim Store
Set Store = CreateObject("CAPICOM.Store")

Store.Open StoreLocation, StoreName, OpenMode

Dim Certificate
Dim Certificates

Set Certificates = (Store.Certificates)

Dim cCerts : cCerts = Certificates.Count

For Each Certificate In Certificates
   Dim binary
   binary = Certificate.export(0)
    WScript.Echo "-----BEGIN CERTIFICATE-----"&Chr(10)&binary&Chr(10)&"-----END CERTIFICATE-----"
Next

' Free resources.
Set Certificates = Nothing

If Err.Number <> 0 Then
	WScript.Quit(-1)
Else 
	WScript quit(0)
End if