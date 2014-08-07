import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import ratpack.handling.Context
import ratpack.form.Form

ratpack {
  handlers {
    get {
      render groovyTemplate("index.html", title: "My Ratpack App")
    }

    get('sendMessageFromPaypal')  {
      def url = new URL("http://localhost:8080/Map/payment")
      connection = (HttpURLConnection)url.openConnection()
      connection.setRequestMethod("POST")
      connection.setDoInput(true)
      connection.setDoOutput(true)

      def file = new File('paypal.txt');
      def urlParameters = file.getText()

      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ())
      wr.writeBytes(urlParameters)
      wr.flush()
      wr.close()

      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      String line;
      StringBuffer response = new StringBuffer(); 
      while((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      rd.close();
      println response

      //"curl POST http://localhost:8080/Map/payment -d @paypal.txt".execute()
      render groovyTemplate("index.html", title: "Sending Message From Paypal")
    }

    post('confirmMessageFromPaypal'){
      Form form = context.parse(Form)
      println form
      response.send "VERIFIED"
      //render groovyTemplate("index.html", title: "Confirm The Message")
    }
        
    assets "public"
  }
}
