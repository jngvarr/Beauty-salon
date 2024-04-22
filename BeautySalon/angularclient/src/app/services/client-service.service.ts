import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Client} from '../model/entities/client';
import {Observable} from 'rxjs/';

@Injectable()
export class ClientService {

  private clientsUrl: string;

  constructor(private http: HttpClient) {
    this.clientsUrl = 'http://localhost:8081/clients';
  }

  public findAll(): Observable<Client[]> {
    return this.http.get<Client[]>(this.clientsUrl);
  }

  public save(client: Client) {
    return this.http.post<Client>(this.clientsUrl + "/create", client);
  }

  public update(updatedClient: Client) {
    return this.http.put<Client>(this.clientsUrl + `/update/${updatedClient.id}`, updatedClient);
  }

  public delete(id: number | undefined) {
    return this.http.delete<Client>(this.clientsUrl + `/delete/${id}`);
  }


}
