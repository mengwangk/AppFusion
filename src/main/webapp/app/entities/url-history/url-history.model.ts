import { BaseEntity, User } from './../../shared';

export class UrlHistory implements BaseEntity {
    constructor(
        public id?: number,
        public url?: string,
        public dateCreated?: any,
        public user?: User,
    ) {
    }
}
