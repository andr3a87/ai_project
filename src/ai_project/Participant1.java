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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrea
 */
public class Participant1 extends Agent {
    protected void setup() {
        DFAgentDescription dfd =new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("type1");
        sd.setName("JADE-Type1");
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
                        ACLMessage reply0 = receive(mt0);
                        if (reply0 != null) {
                            if (reply0.getContent().equals("Buongiorno, come sta?")) {
                                System.out.println(getName() + ": rivevuta CFP! "+reply0.getContent());
                                step++;
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
                  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
              }
        });
    }
}
