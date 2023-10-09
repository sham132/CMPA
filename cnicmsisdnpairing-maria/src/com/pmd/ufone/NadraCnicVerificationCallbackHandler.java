
/**
 * NadraCnicVerificationCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.pmd.ufone;

    /**
     *  NadraCnicVerificationCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class NadraCnicVerificationCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public NadraCnicVerificationCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public NadraCnicVerificationCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for broadcastFamilyTree method
            * override this method for handling normal response from broadcastFamilyTree operation
            */
           public void receiveResultbroadcastFamilyTree(
                    com.pmd.ufone.NadraCnicVerificationStub.BroadcastFamilyTreeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from broadcastFamilyTree operation
           */
            public void receiveErrorbroadcastFamilyTree(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for verification method
            * override this method for handling normal response from verification operation
            */
           public void receiveResultverification(
                    com.pmd.ufone.NadraCnicVerificationStub.VerificationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from verification operation
           */
            public void receiveErrorverification(java.lang.Exception e) {
            }
                


    }
    