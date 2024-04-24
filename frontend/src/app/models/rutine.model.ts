import { ExRutine } from "./exRutine.model";
import {Comment} from "./comment.model"

export interface Rutine
{
  id?: number;
  name: string;
  date: Date;
  time: number;
  lComments?:Comment[];
  exercises?:ExRutine[];
  person:String;
}
