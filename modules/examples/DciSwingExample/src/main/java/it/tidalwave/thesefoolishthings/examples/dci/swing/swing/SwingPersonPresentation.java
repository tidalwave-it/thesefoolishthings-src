/***********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * Copyright (C) 2009-2013 by Tidalwave s.a.s. (http://tidalwave.it)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * WWW: http://thesefoolishthings.java.net
 * SCM: https://bitbucket.org/tidalwave/thesefoolishthings-src
 *
 **********************************************************************************************************************/
package it.tidalwave.thesefoolishthings.examples.dci.swing.swing;

import javax.annotation.Nonnull;
import java.awt.EventQueue;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.beansbinding.BindingGroup;
import it.tidalwave.thesefoolishthings.examples.dci.swing.PersonPresentation;
import it.tidalwave.thesefoolishthings.examples.person.ListOfPersons;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SwingPersonPresentation extends JPanel implements PersonPresentation
  {
    private final BindingGroup bindings = new BindingGroup();

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    public SwingPersonPresentation()
      {
        initComponents();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull Action okAction,
                      final @Nonnull ListOfPersons persons)
      {
        EventQueue.invokeLater(new Runnable()
          {
            public void run()
              {
                Bindings.bind(bindings, persons, liPeople);
                Bindings.bind(bindings, persons, taPeople);
                bindings.bind();
                btOk.setAction(okAction);
              }
          });
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Override
    public void dispose()
      {
        bindings.unbind();
        ((JFrame)(SwingUtilities.getAncestorOfClass(JFrame.class, this))).dispose();
      }

    /*******************************************************************************************************************
     *
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     *
     ******************************************************************************************************************/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btOk = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taPeople = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        liPeople = new javax.swing.JList();

        setName("Form"); // NOI18N

        btOk.setText("Ok");
        btOk.setName("btOk"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        taPeople.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        taPeople.setName("taPeople"); // NOI18N
        jScrollPane1.setViewportView(taPeople);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        liPeople.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        liPeople.setName("liPeople"); // NOI18N
        jScrollPane2.setViewportView(liPeople);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btOk, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(btOk, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btOk;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList liPeople;
    private javax.swing.JTable taPeople;
    // End of variables declaration//GEN-END:variables
  }
