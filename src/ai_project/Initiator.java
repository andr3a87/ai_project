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
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Initiator extends Agent{
    //Title of the book to buy
    private String targetBookTitle;
    //List of seller Agent
    private AID[] sellerAgents;
    
    List<String> l = new ArrayList();
    
    protected void setup() {
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
        System.out.println("Buongiorno, mi presento sono " + getName() + " e sono pronto ad operare!");
        System.out.println("Inizio ricerca film");

        addBehaviour(new TickerBehaviour(this, 6000) {
            private int step = 0;

            @Override
            protected void onTick() {
                switch(step) {
                    case 0:
                        if(l.size() > 0){
                            DFAgentDescription template = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setType("peer");
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
                            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                            for(int i=0; i<sellerAgents.length;i++)
                                cfp.addReceiver(sellerAgents[i]);
                            cfp.setLanguage("Italian");
                            //mando come contenuto del messaggio il nome del primo film
                            cfp.setContent(l.remove(0));
                            send(cfp);
                            System.out.println(getName() + ": inviata CFP, nome film: " + cfp.getContent());
                            step++;
                        }
                        else{
                            block();
                        }
                    break;
                case 1:
                    break;
                case 2:
                break;
                }
    }
     });

    }

}