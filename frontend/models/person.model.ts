export interface Person{
  id?:number;
  alias:string;
  name:string;
  date:string;
  weight:number;
  friends?:string[];
  roles:string[];
  lNotificatios?:Object[];
  rutines?:Object[];
  news?:Object[];
  encodedPassword?:string;
  encodedpassword2?:string;
}
