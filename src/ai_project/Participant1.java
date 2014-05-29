/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai_project;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrea
 * 
 */
public class Participant1 extends Agent {
    
    List<String> l = new ArrayList();
    
    protected void setup() {
        DFAgentDescription dfd =new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        Object[] args = getArguments();
        String nomeFile = (String)args[0];
        String s;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(nomeFile));
            while((s = reader.readLine()) != null ){
                l.add(s);
        }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e){
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, e);
        }
        sd.setType("peer");
        sd.setName("JADE-Peer1");
        dfd.addServices(sd);
        try{
            DFService.register(this, dfd);
        }catch(FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new Behaviour() {
            private int step = 0;

            @Override
            public void action() {
                switch(step) {
                  case 0:
                        MessageTemplate mt0 = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                        ACLMessage msg0 = receive(mt0);
                        if (msg0 != null) {
                            System.out.println(getName+": Ho un messaggio");
                            if (l.contains(msg0.getContent())) {
                                System.out.println(getName() + ": Film Trovato!");
                                ACLMessage reply0 = msg0.createReply();
                                reply0.setPerformative(ACLMessage.PROPOSE);
                                reply0.setContent("1000");
                                send(reply0);
                                System.out.println(getName() + ": inviata PROPOSE");
                                step++;
                            }
                            else{
                                System.out.println(getName() + ": Film NON Trovato!");
                                ACLMessage reply0 = msg0.createReply();
                                reply0.setPerformative(ACLMessage.REFUSE);
                                reply0.setContent("0");
                                send(reply0);
                                System.out.println(getName() + ": inviata Refuse");
                            }
                        } else {
                            block();
                        }
                  break;
                  case 1:
                      
                  break;
                  case 2:
                  break;
                }
            }

              @Override
              public boolean done() {
                  return step == 3;
              }
        });
    }
}
