package com.tax.droidpropa;

public final class Appunto {
	
	 public String idAppunto = "";
	 public String destinatario = "";
	 public String idScuola = "";
	 public String scuola = "";
	 public String telefono = "";
	 public String note = "";
	 public String note2 = "";
	 public String stato = "";
	 public String data = "";
	 
	 public Appunto() {
	 }	 
	 
	 @Override
	 public String toString() {
	   return destinatario + "(" + scuola + ")";
	 }
	 
	 @Override
	 public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	//    result = prime * result + (int) (this.idAppunto ^ (this.idAppunto >>> 32));
	    result = prime * result + ((this.destinatario == null) ? 0 : this.destinatario.hashCode());
	    result = prime * result + ((this.idAppunto == null) ? 0 : this.idAppunto.hashCode());
	//	      result = prime * result + (int) (this.datePubStamp ^ (this.datePubStamp >>> 32));
	//	      result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
	//	      result = prime * result + ((this.format == null) ? 0 : this.format.hashCode());
	//	      result = prime * result + ((this.isbn10 == null) ? 0 : this.isbn10.hashCode());
	//	      result = prime * result + ((this.isbn13 == null) ? 0 : this.isbn13.hashCode());
	//	      result = prime * result + ((this.publisher == null) ? 0 : this.publisher.hashCode());
	//	      result = prime * result + ((this.subTitle == null) ? 0 : this.subTitle.hashCode());
	//	      result = prime * result + ((this.subject == null) ? 0 : this.subject.hashCode());
	//	      result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
	    return result;
	 }
	   
	 public String toStringFull() {
		      StringBuilder sb = new StringBuilder();
		      sb.append("Appunto-");
		      sb.append("\n idAppunto:" + idAppunto);
		      sb.append("\n Destinatario:" + destinatario);
		      sb.append("\n Scuola:" + scuola);
		      sb.append("\n Telefono:" + telefono);
		      sb.append("\n Note:" + note);
		      sb.append("\n Note2:" + note2);
		      sb.append("\n stato:" + stato);
		      sb.append("\n data:" + data);
		      return sb.toString();
	 }
	   
	 @Override
	 public boolean equals(Object obj) {
	      if (this == obj) {
	         return true;
	      }
	      if (obj == null) {
	         return false;
	      }
	      if (!(obj instanceof Appunto)) {
	         return false;
	      }
	      
	      Appunto other = (Appunto) obj;
	      if (this.destinatario == null) {
	         if (other.destinatario != null) {
	            return false;
	         }
	      } else if (!this.destinatario.equals(other.destinatario)) {
	         return false;
	      }
	      if (this.scuola == null) {
	         if (other.scuola != null) {
	            return false;
	         }
	      } else if (!this.scuola.equals(other.scuola)) {
	         return false;
	      }
	      if (this.idScuola == null) {
	         if (other.idScuola != null) {
	            return false;
	         }
	      } else if (!this.idScuola.equals(other.idScuola)) {
	         return false;
	      }
	      if (this.idAppunto != other.idAppunto) {
	         return false;
	      }
	      if (this.telefono == null) {
	         if (other.telefono != null) {
	            return false;
	         }
	      } else if (!this.telefono.equals(other.telefono)) {
	         return false;
	      }
	      if (this.stato == null) {
	         if (other.stato != null) {
	            return false;
	         }
	      } else if (!this.stato.equals(other.stato)) {
	         return false;
	      }
	      if (this.note == null) {
	         if (other.note != null) {
	            return false;
	         }
	      } else if (!this.note.equals(other.note)) {
	         return false;
	      }
	      if (this.note2 == null) {
	         if (other.note2 != null) {
	            return false;
	         }
	      } else if (!this.note2.equals(other.note2)) {
	         return false;
	      }
	      if (this.data == null) {
	         if (other.data != null) {
	            return false;
	         }
	      } else if (!this.data.equals(other.data)) {
	         return false;
	      }
	      return true;
	 }

}
