/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai_project;

/**
 *
 * @author Andrea
 */
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Initiator extends Agent {
    //Title of the book to buy
    private String targetBookTitle;
    //List of seller Agent
    private AID[] sellerAgents;
    
    protected void setup() {
      System.out.println("Buongiorno, mi presento sono " + getName() + " e sono pronto ad operare!");
      Object[] args = getArguments();
      if (args != null && args.length > 0)
        targetBookTitle = (String) args[0];
      System.out.println("Book Title"+targetBookTitle);
      
      addBehaviour(new Behaviour() {
        private int step = 0;
        
        @Override
        public void action() {
            switch(step) {
                case 0:
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("type1");
                    template.addServices(sd);
                    DFAgentDescription[]  result;
                     try {
                        result = DFService.search(myAgent,template);
                        sellerAgents = new AID[result.length];
                        for (int i=0;i<result.length;i++)
                            sellerAgents[i]=result[i].getName();
                     } catch (FIPAException ex) {
                        Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
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