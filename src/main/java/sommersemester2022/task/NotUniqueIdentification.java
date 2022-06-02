package sommersemester2022.task;

/** NotUniqueIdentification schafft die Vorraussetzung das jede Tochterklasse dieses Interfaces die Funktion
 * getNotUniqueId(); besitzt.
 * @author Florian Weinert
 */
public interface NotUniqueIdentification {


  /**
   * Getter-Methode die dafür sorgt das die erstellten UUIDs ausgelesen werden
   * @return einzigartige ID der tasks und der sollution
   */
  String getNotUniqueId();
}
