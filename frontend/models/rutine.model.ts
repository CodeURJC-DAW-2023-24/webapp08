import { ExRutine } from "./exRutine.model";


export interface Rutine
{
  id?: number;
  name: string;
  date: Date;
  time: number;
  lComments?:object[];
  exercises?:ExRutine[];
  person:String;
}
