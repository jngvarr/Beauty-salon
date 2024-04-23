import {Consumable} from '../entities/consumable';


export class Service {
  public id?: number;
  public title?: string;
  public price?: number;
  public duration?: number;
  public description?: string;
  public consumables?: Consumable[];

}
