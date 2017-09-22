import { BaseEntity, User } from './../../shared';

export class UrlHistory implements BaseEntity {
    constructor(
        public id?: number,
        public url?: string,
        public user?: User,
    ) {
    }
}
