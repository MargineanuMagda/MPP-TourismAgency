using System;
using System.Windows.Forms;
using services;

namespace client
{
    partial class Login : Form
    {
        
        ClientController ctrl;
        public Login(ClientController controller)
        {
            InitializeComponent();
            ctrl = controller;

        }

        
        

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {

        }

        private void button3_Click(object sender, EventArgs e)
        {
            string user = textBox5.Text;
            string pass1 = textBox6.Text;
            string pass2 = textBox7.Text;
            if (pass1 != pass2)
            {
                MessageBox.Show("Bouth passwords should be the same!");
            }
            else
            {
                try
                {
                    ctrl.AddUser(user, pass1);
                    ctrl.Login(user, pass1);


                    AgencyWindow app = new AgencyWindow(ctrl);
                    app.ShowDialog();
                    this.Hide();
                }
                catch (ServiceException ex)
                {
                    MessageBox.Show(ex.Message);
                }
                

            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string user = textBox4.Text;
            string passw = textBox3.Text;
            try
            {
                ctrl.Login(user, passw);
                Console.WriteLine("User bun!");
                AgencyWindow app = new AgencyWindow(ctrl);
                app.Show();
                this.Hide();

            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void linkLabel2_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            tabControl1.SelectedIndex = 1;
        }
    }
}
