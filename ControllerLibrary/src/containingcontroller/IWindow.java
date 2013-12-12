/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containingcontroller;

import java.util.Date;

/**
 *
 * @author Hendrik
 */
public interface IWindow {
    public void WriteLogLine(String message);
    public void setTime(Date simTime);
}
