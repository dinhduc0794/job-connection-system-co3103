import React from 'react';
import {Star, MapPin, Phone, Mail, Building2,Award} from 'lucide-react';

import {Card,
    CardHeader,
    CardTitle,
    CardDescription,
    CardContent,
    CardFooter} from '../components/card';
const CompanyCard = ({ company, onClick }) => {
    const renderRating = (rating) => {
      return (
        <div className="flex items-center gap-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <Star
              key={star}
              className={`w-4 h-4 ${
                star <= rating
                  ? 'fill-yellow-400 text-yellow-400'
                  : 'fill-gray-200 text-gray-200'
              }`}
            />
          ))}
          <span className="text-sm text-gray-600 ml-2">{rating.toFixed(1)}</span>
        </div>
      );
    };
  
    return (
      <Card 
        className="hover:shadow-lg transition-shadow cursor-pointer"
        onClick={onClick}
      >
        <CardHeader className="flex flex-row gap-4">
          <div className="w-24 h-24 rounded-lg overflow-hidden flex-shrink-0">
            <img
              src={company.image}
              alt={company.name}
              className="w-full h-full object-cover"
            />
          </div>
          <div className="flex-1">
            <h3 className="text-xl font-semibold text-gray-900 line-clamp-1">
              {company.name}
            </h3>
            {renderRating(company.rating)}
            <div className="flex items-center gap-2 text-gray-600 mt-2">
              <Award className="w-4 h-4" />
              <span className="text-sm">{company.fields}</span>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            <p className="text-gray-600 text-sm line-clamp-2">
              {company.description}
            </p>
            {company.addresses && company.addresses.length > 0 && (
              <div className="flex items-start gap-2">
                <MapPin className="w-4 h-4 text-gray-500 mt-1" />
                <span className="text-sm text-gray-600 flex-1">
                  {company.addresses[0]}
                </span>
              </div>
            )}
            <div className="flex flex-wrap gap-4 mt-3">
              {company.phoneNumbers && company.phoneNumbers.length > 0 && (
                <div className="flex items-center gap-2">
                  <Phone className="w-4 h-4 text-gray-500" />
                  <span className="text-sm text-gray-600">
                    {company.phoneNumbers[0]}
                  </span>
                </div>
              )}
              {company.emails && company.emails.length > 0 && (
                <div className="flex items-center gap-2">
                  <Mail className="w-4 h-4 text-gray-500" />
                  <span className="text-sm text-gray-600">{company.emails[0]}</span>
                </div>
              )}
            </div>
          </div>
        </CardContent>
      </Card>
    );
  };
  export default CompanyCard;