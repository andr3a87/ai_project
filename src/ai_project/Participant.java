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
public class Participant extends Agent {
    
    List<String> l = new ArrayList();
    String speed = new String();
    
    protected void setup() {
        DFAgentDescription dfd =new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        Object[] args = getArguments();
        String nomeFile = (String)args[0];
        speed = (String)args[1];
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
                            System.out.println(getName()+": \tHo un messaggio");
                            if (l.contains(msg0.getContent())) {
                                System.out.println(getName() + ": \tFilm Trovato!");
                                ACLMessage reply0 = msg0.createReply();
                                reply0.setPerformative(ACLMessage.PROPOSE);
                                reply0.setContent(speed);
                                send(reply0);
                                System.out.println(getName() + ": \tinviata PROPOSE");
                                step++;
                            }
                            else{
                                System.out.println(getName() + ": \tFilm NON Trovato!");
                                ACLMessage reply0 = msg0.createReply();
                                reply0.setPerformative(ACLMessage.REFUSE);
                                reply0.setContent("0");
                                send(reply0);
                                System.out.println(getName() + ": \tinviata Refuse");
                            }
                        } else {
                            block();
                        }
                  break;
                  case 1:
                        MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        ACLMessage msg1 = receive(mt1);
                        if (msg1 != null) {
                            System.out.println(getName()+": \tHo un messaggio");
                            System.out.println(getName() + ": \tsceglie me!");
                            
                            ACLMessage reply1 = msg1.createReply();
                            reply1.setPerformative(ACLMessage.INFORM);
                            reply1.setContent("done");
                            send(reply1);
                            System.out.println(getName() + ": \tinviata Done");
                            step = 0;
                        } else {
                            block();
                        }
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
